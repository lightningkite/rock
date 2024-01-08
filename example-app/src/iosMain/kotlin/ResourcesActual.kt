package com.lightningkite.mppexampleapp

import com.lightningkite.rock.models.*

actual object Resources {
    actual val fontsMontserrat: Font = fontFromFamilyInfo(normal = "Montserrat-Regular", italic = "Montserrat-Italic", bold = "Montserrat-Bold", boldItalic = "Montserrat-BoldItalic")  // Font(name=fontsMontserrat, normal=SubFont(source=/Users/jivie/Projects/rock/example-app/src/commonMain/resources/fonts/montserrat/normal.ttf, relativeFile=fonts/montserrat/normal.ttf, fontSuperFamily=, fontFamily=Montserrat, fontSubFamily=Regular, postScriptName=Montserrat-Regular), bold=SubFont(source=/Users/jivie/Projects/rock/example-app/src/commonMain/resources/fonts/montserrat/bold.ttf, relativeFile=fonts/montserrat/bold.ttf, fontSuperFamily=, fontFamily=Montserrat, fontSubFamily=Bold, postScriptName=Montserrat-Bold), italic=SubFont(source=/Users/jivie/Projects/rock/example-app/src/commonMain/resources/fonts/montserrat/italic.ttf, relativeFile=fonts/montserrat/italic.ttf, fontSuperFamily=, fontFamily=Montserrat, fontSubFamily=Italic, postScriptName=Montserrat-Italic), boldItalic=SubFont(source=/Users/jivie/Projects/rock/example-app/src/commonMain/resources/fonts/montserrat/bold-italic.ttf, relativeFile=fonts/montserrat/bold-italic.ttf, fontSuperFamily=, fontFamily=Montserrat, fontSubFamily=Bold Italic, postScriptName=Montserrat-BoldItalic))
    actual val imagesMammoth: ImageResource = ImageResource("imagesMammoth")
    actual val imagesSolera: ImageResource = ImageResource("imagesSolera")
}