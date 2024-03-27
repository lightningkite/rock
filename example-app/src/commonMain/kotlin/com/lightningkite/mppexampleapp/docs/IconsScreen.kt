package com.lightningkite.mppexampleapp.docs

import com.lightningkite.kiteui.Routable
import com.lightningkite.kiteui.models.Icon
import com.lightningkite.kiteui.views.*
import com.lightningkite.kiteui.views.direct.*
import com.lightningkite.kiteui.models.rem
import com.lightningkite.kiteui.views.l2.icon

@Routable("docs/icons")
object IconsScreen: DocScreen {
    override val covers: List<String> = listOf("icons","Icons")
    val Icon.Companion.fingerPrintCustom get() = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M130-574q-7-5-8.5-12.5T126-602q62-85 155.5-132T481-781q106 0 200 45.5T838-604q7 9 4.5 16t-8.5 12q-6 5-14 4.5t-14-8.5q-55-78-141.5-119.5T481-741q-97 0-182 41.5T158-580q-6 9-14 10t-14-4ZM594-81q-104-26-170-103.5T358-374q0-50 36-84t87-34q51 0 87 34t36 84q0 33 25 55.5t59 22.5q34 0 58-22.5t24-55.5q0-116-85-195t-203-79q-118 0-203 79t-85 194q0 24 4.5 60t21.5 84q3 9-.5 16T208-205q-8 3-15.5-.5T182-217q-15-39-21.5-77.5T154-374q0-133 96.5-223T481-687q135 0 232 90t97 223q0 50-35.5 83.5T688-257q-51 0-87.5-33.5T564-374q0-33-24.5-55.5T481-452q-34 0-58.5 22.5T398-374q0 97 57.5 162T604-121q9 3 12 10t1 15q-2 7-8 12t-15 3ZM260-783q-8 5-16 2.5T232-791q-4-8-2-14.5t10-11.5q56-30 117-46t124-16q64 0 125 15.5T724-819q9 5 10.5 12t-1.5 14q-3 7-10 11t-17-1q-53-27-109.5-41.5T481-839q-58 0-114 13.5T260-783ZM378-95q-59-62-90.5-126.5T256-374q0-91 66-153.5T481-590q93 0 160 62.5T708-374q0 9-5.5 14.5T688-354q-8 0-14-5.5t-6-14.5q0-75-55.5-125.5T481-550q-76 0-130.5 50.5T296-374q0 81 28 137.5T406-123q6 6 6 14t-6 14q-6 6-14 6t-14-6Zm302-68q-89 0-154.5-60T460-374q0-8 5.5-14t14.5-6q9 0 14.5 6t5.5 14q0 75 54 123t126 48q6 0 17-1t23-3q9-2 15.5 2.5T744-191q2 8-3 14t-13 8q-18 5-31.5 5.5t-16.5.5Z"))

    override fun ViewWriter.render() {
        article {
            h1("Icons")
            h2("Built-in Icons")
            text("You can use the built-in icons to add common icons to your app.")
            example("""
                         icon(Icon.home, "Forward")
                         icon {
                             source = Icon.arrowBack
                             description = "Back"

                         }
                         icon(Icon.search, "Search")
                         icon(Icon.menu, "Menu")
                """.trimIndent()) {
                centered - stack {
                     row {
                         icon(Icon.home, "Forward")
                         icon {
                             source = Icon.arrowBack
                             description = "Back"

                         }
                         icon(Icon.search, "Search")
                         icon(Icon.menu, "Menu")
                     }
                }
            }
            h2("Custom Icons")
            text("Use SVG to create Icons to use in the app.")
            text("You will need to extract the minX, minY, maxX, maxY, width, and height from the SVG file and use them in the parameters of the Icon constructor.")
            text("You will also need to extract the paths from the SVG file and place them as list of strings in the pathDatas parameter of the Icon constructor.")
            val customIcon = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M280-80v-160H0l154-240H80l280-400 120 172 120-172 280 400h-74l154 240H680v160H520v-160h-80v160H280Zm389-240h145L659-560h67L600-740l-71 101 111 159h-74l103 160Zm-523 0h428L419-560h67L360-740 234-560h67L146-320Zm0 0h155-67 252-67 155-428Zm523 0H566h74-111 197-67 155-145Zm-149 80h160-160Zm201 0Z"))
            example("""
                      icon(
                            {
                                Icon(
                                    2.rem,
                                    2.rem,
                                    0,
                                    -960,
                                    960,
                                    960,
                                    listOf("M200-120q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h560q33 0 56.5 23.5T840-760v560q0 33-23.5 56.5T760-120H200Zm0-80h560v-560H200v560Zm40-80h480L570-480 450-320l-90-120-120 160Zm-40 80v-560 560Z")
                                )
                            },
                            "This is a custom icon"
                        )
                
                         val customIcon = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M200-120q- 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h560q33 0 56.5 23.5T840-760v560q0 33-23.5 56.5T760-120H200Zm0-80h560v-560H200v560Zm40-80h480L570-480 450-320l-90-120-120 160Zm-40 80v-560 560Z"))
                            
                            
                         val Icon.Companion.fingerPrintCustom get() = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M130-574q-7-5-8.5-12.5T126-602q62-85 155.5-132T481-781q106 0 200 45.5T838-604q7 9 4.5 16t-8.5 12q-6 5-14 4.5t-14-8.5q-55-78-141.5-119.5T481-741q-97 0-182 41.5T158-580q-6 9-14 10t-14-4ZM594-81q-104-26-170-103.5T358-374q0-50 36-84t87-34q51 0 87 34t36 84q0 33 25 55.5t59 22.5q34 0 58-22.5t24-55.5q0-116-85-195t-203-79q-118 0-203 79t-85 194q0 24 4.5 60t21.5 84q3 9-.5 16T208-205q-8 3-15.5-.5T182-217q-15-39-21.5-77.5T154-374q0-133 96.5-223T481-687q135 0 232 90t97 223q0 50-35.5 83.5T688-257q-51 0-87.5-33.5T564-374q0-33-24.5-55.5T481-452q-34 0-58.5 22.5T398-374q0 97 57.5 162T604-121q9 3 12 10t1 15q-2 7-8 12t-15 3ZM260-783q-8 5-16 2.5T232-791q-4-8-2-14.5t10-11.5q56-30 117-46t124-16q64 0 125 15.5T724-819q9 5 10.5 12t-1.5 14q-3 7-10 11t-17-1q-53-27-109.5-41.5T481-839q-58 0-114 13.5T260-783ZM378-95q-59-62-90.5-126.5T256-374q0-91 66-153.5T481-590q93 0 160 62.5T708-374q0 9-5.5 14.5T688-354q-8 0-14-5.5t-6-14.5q0-75-55.5-125.5T481-550q-76 0-130.5 50.5T296-374q0 81 28 137.5T406-123q6 6 6 14t-6 14q-6 6-14 6t-14-6Zm302-68q-89 0-154.5-60T460-374q0-8 5.5-14t14.5-6q9 0 14.5 6t5.5 14q0 75 54 123t126 48q6 0 17-1t23-3q9-2 15.5 2.5T744-191q2 8-3 14t-13 8q-18 5-31.5 5.5t-16.5.5Z"))
                        
                        
                         override fun ViewWriter.render() {
                                icon {
                                    source = Icon.fingerPrintCustom
                                    description = "This is a custom icon"
                                }
                        }
                            
                """.trimIndent()) {
               centered - card
                    row {
                        icon(
                            {
                                Icon(
                                    2.rem,
                                    2.rem,
                                    0,
                                    -960,
                                    960,
                                    960,
                                    listOf("M200-120q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h560q33 0 56.5 23.5T840-760v560q0 33-23.5 56.5T760-120H200Zm0-80h560v-560H200v560Zm40-80h480L570-480 450-320l-90-120-120 160Zm-40 80v-560 560Z")
                                )
                            },
                            "This is a custom icon"
                        )
                        icon(customIcon, "This is a custom icon")
                        icon {
                            source = Icon.fingerPrintCustom
                            description = "This is a custom icon"
                        }
                    }

                }
            }
        }

}