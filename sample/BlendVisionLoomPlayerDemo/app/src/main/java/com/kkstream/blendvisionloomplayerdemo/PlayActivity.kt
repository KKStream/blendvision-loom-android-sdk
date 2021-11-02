package com.kkstream.blendvisionloomplayerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.kkstream.blendvisionloom.player.BlendVisionLoomPlayer
import com.kkstream.blendvisionloom.player.configuration.MediaItem
import com.kkstream.blendvisionloom.player.configuration.PlayerContext
import com.kkstream.blendvisionloom.player.controller.BlendVisionPlayerController
import com.kkstream.blendvisionloom.player.error.ErrorEvent
import com.kkstream.blendvisionloom.player.eventListener.EventListener

class PlayActivity : AppCompatActivity() {

    val JWT: String = "JWT_TOKEN"

    val mediaItems: List<MediaItem> =
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

    var controller: BlendVisionPlayerController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val container: ViewGroup = findViewById<FrameLayout>(R.id.container)

        BlendVisionLoomPlayer.presentPlayer(
            playerContext = PlayerContext(
                activity = this,
                container = container
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
                    // `previous`, `next`
                    // `rewind` and `forward`
                    // `showController`
                    // `seek`
                    // `setBackgroundPlay`
                    // `enterFullScreen`
                    // `exitFullScreen`
                    // `isFullscreen`
                    // `replay`
                }

                override fun onPlaybackEnd(index: Int) {
                    // logic when playback END for the media at [index]
                }

                override fun onError(error: ErrorEvent): Boolean {
                    return false
                }
            }
        )
    }
}