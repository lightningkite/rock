package com.lightningkite.rock.objc

import com.lightningkite.rock.objc.cgColorRefasIdType
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGColorRef

@OptIn(ExperimentalForeignApi::class)
fun CGColorRef.toObjcId(): Any = cgColorRefasIdType(this)!!