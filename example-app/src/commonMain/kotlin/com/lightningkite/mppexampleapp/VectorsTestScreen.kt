package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.fetch
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.bind
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("test/vectors")
object VectorsTestScreen : RockScreen {
    override fun ViewWriter.render() {
        col {
            row {
                image {
                    source = ImageVector(
                        4.rem,
                        4.rem,
                        viewBoxWidth = 100,
                        viewBoxHeight = 100,
                        paths = listOf(
                            ImageVector.Path(
                                fillColor = Color.green,
                                path = "M 0,0 L 0,100 L 100,100 L 100,0 Z"
                            ),
                            ImageVector.Path(
                                strokeColor = Color.blue,
                                strokeWidth = 5.0,
                                path = "M 25, 50 l 50,0 l -50,0 z"
                            ),
                            ImageVector.Path(
                                fillColor = Color.red,
                                strokeWidth = 2.0,
                                path = "M 25, 50 a 25,25 0 1,1 50,0 a 25,25 0 1,1 -50,0z"
                            )
                        )
                    )
                }
                image {
                    source = ImageVector(
                        4.rem,
                        4.rem,
                        viewBoxWidth = 100,
                        viewBoxHeight = 100,
                        paths = listOf(
                            ImageVector.Path(
                                fillColor = Color.green,
                                path = "M 0,0 L 0,100 L 100,100 L 100,0 Z"
                            ),
                            ImageVector.Path(
                                strokeColor = Color.blue,
                                strokeWidth = 5.0,
                                path = "M 0, 0 Q 100, 0, 100, 100 L 0, 100 z"
                            ),
                        )
                    )
                }
                image {
                    source = ImageVector(
                        4.rem,
                        4.rem,
                        viewBoxWidth = 100,
                        viewBoxHeight = 100,
                        paths = listOf(
                            ImageVector.Path(
                                fillColor = Color.green,
                                path = "M 0,0 L 0,100 L 100,100 L 100,0 Z"
                            ),
                            ImageVector.Path(
                                strokeColor = Color.blue,
                                strokeWidth = 5.0,
                                path = "M 50, 0 Q 100, 0, 100, 50 T 50 100 T 0 50 z"
                            ),
                        )
                    )
                }
                image {
                    source = ImageVector(
                        4.rem,
                        4.rem,
                        viewBoxWidth = 100,
                        viewBoxHeight = 100,
                        paths = listOf(
                            ImageVector.Path(
                                fillColor = Color.green,
                                path = "M 0,0 L 0,100 L 100,100 L 100,0 Z"
                            ),
                            ImageVector.Path(
                                strokeColor = Color.blue,
                                strokeWidth = 5.0,
                                path = "M 0, 0 C 100, 0, 0, 100, 100, 100 L 0, 100 z"
                            ),
                        )
                    )
                }
                image {
                    source = ImageVector(
                        4.rem,
                        4.rem,
                        viewBoxWidth = 100,
                        viewBoxHeight = 100,
                        paths = listOf(
                            ImageVector.Path(
                                fillColor = Color.green,
                                path = "M 0,0 L 0,100 L 100,100 L 100,0 Z"
                            ),
                            ImageVector.Path(
                                strokeColor = Color.blue,
                                strokeWidth = 5.0,
                                path = "M 50, 0 C 75, 0, 100, 25, 100, 50 S 100 100 50 100 S 0 100 0 50 z"
                            ),
                        )
                    )
                }
            }
            image {
                source = Icon.settings.copy(width = 20.rem, height = 20.rem).toImageSource(fillColor = Color.red)
            }
        }
    }
}