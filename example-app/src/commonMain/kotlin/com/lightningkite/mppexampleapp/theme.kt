package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.Color
import com.lightningkite.mppexample.PaintPair
import com.lightningkite.mppexample.Theme
import com.lightningkite.mppexample.systemDefaultFont

val appTheme = Theme(
    titleFont = systemDefaultFont,
    bodyFont = systemDefaultFont,
    baseSize = 18.0,
    normal = PaintPair(
        foreground = Color.black, background = Color.white
    ),
    primary = PaintPair(
        foreground = Color.white, background = Color.fromHex(0x1566B7)
    ),
    accent = PaintPair(
        foreground = Color.white,
        background = Color.fromHex(0x9C27B0),
    ),
)
