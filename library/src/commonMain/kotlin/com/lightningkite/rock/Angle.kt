package com.lightningkite.rock

import com.lightningkite.rock.models.positiveRemainder
import kotlin.jvm.JvmInline
import kotlin.math.PI
import kotlin.math.absoluteValue

@JvmInline
value class Angle(val turns: Float) {
    companion object {
        const val RADIANS_PER_CIRCLE = (PI * 2).toFloat()
        const val DEGREES_PER_CIRCLE = 360f
        fun degrees(degrees: Float): Angle = Angle(degrees / DEGREES_PER_CIRCLE)
        fun radians(radians: Float): Angle = Angle(radians / RADIANS_PER_CIRCLE)
        fun atan2(y: Float, x: Float) = radians(kotlin.math.atan2(y, x))
        val zero = Angle(0f)
        val circle = Angle(1f)
        val halfTurn = Angle(.5f)
        val quarterTurn = Angle(.25f)
        val eighthTurn = Angle(.125f)
        val thirdTurn = Angle(1 / 3.0f)
    }

    inline val degrees: Float get() = turns * DEGREES_PER_CIRCLE
    inline val radians: Float get() = turns * RADIANS_PER_CIRCLE

    //For absolute angles
    inline infix fun angleTo(other: Angle): Angle {
        return Angle((other.turns - this.turns + .5f).positiveRemainder(1f) - .5f)
    }

    //For relative angles
    inline operator fun plus(other: Angle): Angle = Angle(this.turns + other.turns)

    inline operator fun minus(other: Angle): Angle = Angle(this.turns - other.turns)
    inline operator fun times(scale: Float) = Angle(this.turns * scale)
    inline operator fun div(by: Float) = Angle(this.turns / by)

    fun normalized(): Angle = Angle(this.turns.plus(.5f).positiveRemainder(1f).minus(.5f))

    inline fun sin(): Float = kotlin.math.sin(radians)
    inline fun cos(): Float = kotlin.math.cos(radians)
    inline fun tan(): Float = kotlin.math.tan(radians)

    inline operator fun unaryMinus() = Angle(-turns)

    val absoluteValue: Angle get() = Angle(turns.absoluteValue)
}