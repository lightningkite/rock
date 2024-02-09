package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.docs

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.VideoRemote
import com.lightningkite.rock.models.dp
import com.lightningkite.rock.models.rem
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("docs/video")
object VideoScreen: DocScreen {
    override val covers: List<String> = listOf("video", "Video")

    override fun ViewWriter.render() {
        scrolls - col {
            h1("Video")
            text("You can use the video element to render video, streamed from a remote source or locally.")
            val time = Property(0.0)
            val playing = Property(false)
            example("""
                val time = Property(0.0)
                val playing = Property(false)
                video {
                    source = VideoRemote("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
                    this.time bind time
                    this.playing bind playing
                }
                """.trimIndent()) {
                sizeConstraints(height = 10.rem) - video {
                    source = VideoRemote("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
                    this.time bind time
                    this.playing bind playing
                }
            }
            text("You can observe or control the current time via 'time'.")
            example("""
                col {
                    text { ::content { "Time: ${'$'}{time.await()}" } }
                    button { 
                        text("Restart")
                        onClick { time set 0.0 }
                    }
                }
                """.trimIndent()) {
                col {
                    text { ::content { "Time: ${time.await()}" } }
                    button {
                        text("Restart")
                        onClick { time set 0.0 }
                    }
                }
            }
            text("You can observe or control the playing state via 'playing'.")
            example("""
                col {
                    text { ::content { if(playing.await()) "Playing" else "Paused" } }
                    button {
                        text("Play")
                        onClick { playing set true }
                    }
                    button {
                        text("Pause")
                        onClick { playing set false }
                    }
                }
                """.trimIndent()) {
                col {
                    text { ::content { if(playing.await()) "Playing" else "Paused" } }
                    button {
                        text("Play")
                        onClick { playing set true }
                    }
                    button {
                        text("Pause")
                        onClick { playing set false }
                    }
                }
            }
        }
    }

}