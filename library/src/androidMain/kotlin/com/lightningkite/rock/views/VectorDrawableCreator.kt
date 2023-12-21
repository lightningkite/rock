package com.lightningkite.rock.views

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.util.Xml
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import org.xmlpull.v1.XmlPullParser
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets
import java.util.Arrays


object VectorDrawableCreator {
    private val BIN_XML_STRINGS = arrayOf(
        "width".toByteArray(),
        "height".toByteArray(),
        "viewportWidth".toByteArray(),
        "viewportHeight".toByteArray(),
        "fillColor".toByteArray(),
        "pathData".toByteArray(),
        "path".toByteArray(),
        "vector".toByteArray(),
        "http://schemas.android.com/apk/res/android".toByteArray()
    )
    private val BIN_XML_ATTRS = intArrayOf(
        R.attr.height,
        R.attr.width,
        R.attr.viewportWidth,
        R.attr.viewportHeight,
        R.attr.fillColor,
        R.attr.pathData
    )
    private const val CHUNK_TYPE_XML: Short = 0x0003
    private const val CHUNK_TYPE_STR_POOL: Short = 0x0001
    private const val CHUNK_TYPE_START_TAG: Short = 0x0102
    private const val CHUNK_TYPE_END_TAG: Short = 0x0103
    private const val CHUNK_TYPE_RES_MAP: Short = 0x0180
    private const val VALUE_TYPE_DIMENSION: Short = 0x0500
    private const val VALUE_TYPE_STRING: Short = 0x0300
    private const val VALUE_TYPE_COLOR: Short = 0x1D00
    private const val VALUE_TYPE_FLOAT: Short = 0x0400

    /**
     * Create a vector drawable from a list of paths and colors
     * @param width drawable width
     * @param height drawable height
     * @param viewportWidth vector image width
     * @param viewportHeight vector image height
     * @param paths list of path data and colors
     * @return the vector drawable or null it couldn't be created.
     */
    fun getVectorDrawable(
        context: Context,
        width: Int, height: Int,
        viewportWidth: Float, viewportHeight: Float,
        paths: List<PathData>
    ): Drawable? {
        val binXml = createBinaryDrawableXml(width, height, viewportWidth, viewportHeight, paths)
        try {
            // Get the binary XML parser (XmlBlock.Parser) and use it to create the drawable
            // This is the equivalent of what AssetManager#getXml() does
            @SuppressLint("PrivateApi") val xmlBlock = Class.forName("android.content.res.XmlBlock")
            val xmlBlockConstr = xmlBlock.getConstructor(ByteArray::class.java)
            val xmlParserNew = xmlBlock.getDeclaredMethod("newParser")
            xmlBlockConstr.isAccessible = true
            xmlParserNew.isAccessible = true
            val parser = xmlParserNew.invoke(
                xmlBlockConstr.newInstance(binXml as Any)
            ) as XmlPullParser
            return Drawable.createFromXml(context.resources, parser)
        } catch (e: Exception) {
            Log.e("VectorDrawableCreator", "Vector creation failed", e)
        }
        return null
    }

    private fun createBinaryDrawableXml(
        width: Int, height: Int,
        viewportWidth: Float, viewportHeight: Float,
        paths: List<PathData>
    ): ByteArray {
        val stringPool: MutableList<ByteArray> = ArrayList(Arrays.asList(*BIN_XML_STRINGS))
        for (path in paths) {
            stringPool.add(path.data)
        }
        val bb = ByteBuffer.allocate(8192) // Capacity might have to be greater.
        bb.order(ByteOrder.LITTLE_ENDIAN)
        var posBefore: Int

        // ==== XML chunk ====
        // https://justanapplication.wordpress.com/2011/09/22/android-internals-binary-xml-part-two-the-xml-chunk/
        bb.putShort(CHUNK_TYPE_XML) // Type
        bb.putShort(8.toShort()) // Header size
        val xmlSizePos = bb.position()
        bb.position(bb.position() + 4)

        // ==== String pool chunk ====
        // https://justanapplication.wordpress.com/2011/09/15/android-internals-resources-part-four-the-stringpool-chunk/
        val spStartPos = bb.position()
        bb.putShort(CHUNK_TYPE_STR_POOL) // Type
        bb.putShort(28.toShort()) // Header size
        val spSizePos = bb.position()
        bb.position(bb.position() + 4)
        bb.putInt(stringPool.size) // String count
        bb.putInt(0) // Style count
        bb.putInt(1 shl 8) // Flags set: encoding is UTF-8
        val spStringsStartPos = bb.position()
        bb.position(bb.position() + 4)
        bb.putInt(0) // Styles start

        // String offsets
        var offset = 0
        for (str in stringPool) {
            bb.putInt(offset)
            offset += str.size + if (str.size > 127) 5 else 3
        }
        posBefore = bb.position()
        bb.putInt(spStringsStartPos, bb.position() - spStartPos)
        bb.position(posBefore)

        // String pool
        for (str in stringPool) {
            if (str.size > 127) {
                val high = (str.size and 0xFF00 or 0x8000 ushr 8).toByte()
                val low = (str.size and 0xFF).toByte()
                bb.put(high)
                bb.put(low)
                bb.put(high)
                bb.put(low)
            } else {
                val len = str.size.toByte()
                bb.put(len)
                bb.put(len)
            }
            bb.put(str)
            bb.put(0.toByte())
        }
        if (bb.position() % 4 != 0) {
            // Padding to align on 32-bit
            bb.put(ByteArray(4 - bb.position() % 4))
        }

        // Write string pool chunk size
        posBefore = bb.position()
        bb.putInt(spSizePos, bb.position() - spStartPos)
        bb.position(posBefore)

        // ==== Resource map chunk ====
        // https://justanapplication.wordpress.com/2011/09/23/android-internals-binary-xml-part-four-the-xml-resource-map-chunk/
        bb.putShort(CHUNK_TYPE_RES_MAP) // Type
        bb.putShort(8.toShort()) // Header size
        bb.putInt(8 + BIN_XML_ATTRS.size * 4) // Chunk size
        for (attr in BIN_XML_ATTRS) {
            bb.putInt(attr)
        }

        // ==== Vector start tag ====
        val vstStartPos = bb.position()
        val vstSizePos = putStartTag(bb, 7, 4)

        // Attributes
        // android:width="24dp", value type: dimension (dp)
        putAttribute(bb, 0, -1, VALUE_TYPE_DIMENSION, (width shl 8) + 1)

        // android:height="24dp", value type: dimension (dp)
        putAttribute(bb, 1, -1, VALUE_TYPE_DIMENSION, (height shl 8) + 1)

        // android:viewportWidth="24", value type: float
        putAttribute(bb, 2, -1, VALUE_TYPE_FLOAT, java.lang.Float.floatToRawIntBits(viewportWidth))

        // android:viewportHeight="24", value type: float
        putAttribute(bb, 3, -1, VALUE_TYPE_FLOAT, java.lang.Float.floatToRawIntBits(viewportHeight))

        // Write vector start tag chunk size
        posBefore = bb.position()
        bb.putInt(vstSizePos, bb.position() - vstStartPos)
        bb.position(posBefore)
        for (i in paths.indices) {
            // ==== Path start tag ====
            val pstStartPos = bb.position()
            val pstSizePos = putStartTag(bb, 6, 2)

            // android:fillColor="#aarrggbb", value type: #rgb.
            putAttribute(bb, 4, -1, VALUE_TYPE_COLOR, paths[i].color)

            // android:pathData="...", value type: string
            putAttribute(bb, 5, 9 + i, VALUE_TYPE_STRING, 9 + i)

            // Write path start tag chunk size
            posBefore = bb.position()
            bb.putInt(pstSizePos, bb.position() - pstStartPos)
            bb.position(posBefore)

            // ==== Path end tag ====
            putEndTag(bb, 6)
        }

        // ==== Vector end tag ====
        putEndTag(bb, 7)

        // Write XML chunk size
        posBefore = bb.position()
        bb.putInt(xmlSizePos, bb.position())
        bb.position(posBefore)

        // Return binary XML byte array
        val binXml = ByteArray(bb.position())
        bb.rewind()
        bb[binXml]
        return binXml
    }

    private fun putStartTag(bb: ByteBuffer, name: Int, attributeCount: Int): Int {
        // https://justanapplication.wordpress.com/2011/09/25/android-internals-binary-xml-part-six-the-xml-start-element-chunk/
        bb.putShort(CHUNK_TYPE_START_TAG)
        bb.putShort(16.toShort()) // Header size
        val sizePos = bb.position()
        bb.putInt(0) // Size, to be set later
        bb.putInt(0) // Line number: None
        bb.putInt(-1) // Comment: None
        bb.putInt(-1) // Namespace: None
        bb.putInt(name)
        bb.putShort(0x14.toShort()) // Attributes start offset
        bb.putShort(0x14.toShort()) // Attributes size
        bb.putShort(attributeCount.toShort()) // Attribute count
        bb.putShort(0.toShort()) // ID attr: none
        bb.putShort(0.toShort()) // Class attr: none
        bb.putShort(0.toShort()) // Style attr: none
        return sizePos
    }

    private fun putEndTag(bb: ByteBuffer, name: Int) {
        // https://justanapplication.wordpress.com/2011/09/26/android-internals-binary-xml-part-seven-the-xml-end-element-chunk/
        bb.putShort(CHUNK_TYPE_END_TAG)
        bb.putShort(16.toShort()) // Header size
        bb.putInt(24) // Chunk size
        bb.putInt(0) // Line number: none
        bb.putInt(-1) // Comment: none
        bb.putInt(-1) // Namespace: none
        bb.putInt(name) // Name: vector
    }

    private fun putAttribute(
        bb: ByteBuffer, name: Int,
        rawValue: Int, valueType: Short, valueData: Int
    ) {
        // https://justanapplication.wordpress.com/2011/09/19/android-internals-resources-part-eight-resource-entries-and-values/#struct_Res_value
        bb.putInt(8) // Namespace index in string pool (always the android namespace)
        bb.putInt(name)
        bb.putInt(rawValue)
        bb.putShort(0x08.toShort()) // Value size
        bb.putShort(valueType)
        bb.putInt(valueData)
    }

    class PathData(var data: ByteArray, var color: Int) {
        constructor(data: String, color: Int) : this(
            data.toByteArray(StandardCharsets.UTF_8),
            color
        )
    }
}

