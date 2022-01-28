package com.kkstream.blendvisionloomplayerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.kkstream.blendvisionloom.player.BlendVisionLoomPlayer
import com.kkstream.blendvisionloom.player.configuration.Configuration
import com.kkstream.blendvisionloom.player.configuration.MediaItem
import com.kkstream.blendvisionloom.player.configuration.PlayerContext
import com.kkstream.blendvisionloom.player.controller.BlendVisionPlayerController
import com.kkstream.blendvisionloom.player.error.ErrorEvent
import com.kkstream.blendvisionloom.player.eventListener.EventListener
import com.kkstream.blendvisionloom.player.log.VideoEvent

class PlayActivity : AppCompatActivity() {

    private val JWT: String = "JWT_TOKEN"

    private val mediaItems: List<MediaItem> =
        listOf(
            MediaItem(
                contentId = "CONTENT_ID",
                title = "TITLE",
                drm = MediaItem.DrmConfiguration(
                    licenseUrl = "LICENSE_URL",
                    header = mapOf(
                        "Authorization" to "Bearer $JWT"
                    ),
                ),
                startTimeMs = 0,
                sources = listOf(
                    MediaItem.Source(
                        thumbnailSeekingUrl = "THUMBNAIL_SEEKING_URL",
                        manifests = listOf(
                            MediaItem.Manifest.createDash(
                                url = "DASH_MPD_URL"
                            )
                        )
                    )
                )
            )
        )

    private var controller: BlendVisionPlayerController? = null

    private val configuration: Configuration by lazy {
        Configuration(
            isPreCacheEnable = false, // set true to enable pre-caching function,
                                      // then call `cancelPreCacheAndPlay` to cancel pre-caching and start playback
            maxBufferMs = 90000 // set the maximum duration of a playback
                                // that the player will attempt to buffer, in milliseconds.
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val container: ViewGroup = findViewById<FrameLayout>(R.id.container)

        BlendVisionLoomPlayer.presentPlayer(
            playerContext = PlayerContext(
                activity = this,
                container = container,
                configuration = configuration
            ),
            mediaItems = mediaItems,
            // The beginning index [0, mediaItems.length()) of media items.
            startIndex = 0,
            // The listener for any changes in the player.
            eventListener = object : EventListener {
                override fun onPlayerReady(controller: BlendVisionPlayerController) {
                    this@PlayActivity.controller = controller
                    // Application can get the instance of controller from this callback
                    // and then use the controller to perform the following operations:
                    // `play` and `pause`
                    // `isPlaying`
                    // `previous`, `next`
                    // `rewind` and `forward`
                    // `showController`
                    // `seek`
                    // `setBackgroundPlay`
                    // `enterFullScreen`
                    // `exitFullScreen`
                    // `isFullscreen`
                    // `replay`
                    // `mute` and `unmute`
                    // `release` and `restart`
                    // `cancelPreCacheAndPlay`
                }

                override fun onPlaybackReady(index: Int) {
                    // logic when playback is ready to play after buffered enough duration for the media at [index]
                }

                override fun onPlaybackEnd(index: Int) {
                    // logic when playback is end for the media at [index]
                }

                override fun onError(error: ErrorEvent): Boolean {
                    // handle the following errors:
                    // `BasicNetworkError`
                    // `ServerResponseError`
                    // `PlayerError`
                    // `PaaSInternalError`
                    // `StartIndexOutOfRangeError`
                    // `EmptyMediaItemError`
                    // `UndefinedError`
                    //
                    // return `true` to indicate that you have handled the event;
                    // return `false` if you have not handled it,
                    // and the error message displays on the built-in error view.
                    return false
                }


                override fun onVideoEvent(videoEvent: VideoEvent) {
                    // handle the following video events:
                    // `VideoPlaybackBegan`
                    // `VideoPlaybackEnded`
                    // `VideoPlaybackStopped`
                    // `VideoPlaybackErrorOccurred`
                    // `Play`
                    // `Pause`
                    // `Rewind`
                    // `Forward`
                    // `NextEpisode`
                    // `PreviousEpisode`
                    // `VideoSeekingEnded`
                    // `SettingPageEntered`
                    // `SettingPageExited`
                }
            }
        )
    }
}