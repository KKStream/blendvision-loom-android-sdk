package com.kkstream.blendvisionloomplayerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
            maxBufferMs = 90000, // set the maximum duration of a playback
                                // that the player will attempt to buffer, in milliseconds.
            isD3Mode = false    // trigger the D3 mode for LL-DASH
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
                    Log.i(TAG, "onPlayerReady: ")
                    this@PlayActivity.controller = controller
                    // Called when the player is setup and the controller is available
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
                    // `cancelPreCache`
                    // `getPosition`
                    // `getBuffered`
                    // `getDuration`
                }

                override fun onPlaybackReady(index: Int) {
                    Log.i(TAG, "onPlaybackReady: index=$index")
                    // Called when the playback is able to immediately play from its current position for the media at [index]
                }

                override fun onPlaybackReady(index: Int, playWhenReady: Boolean) {
                    Log.i(TAG, "onPlaybackReady: index=$index, playWhenReady=$playWhenReady")
                    // Called when the playback is able to immediately play from its current position with
                    // the playWhenReady flag to indicate whether the playback will proceed for the media at [index]
                }

                override fun onPlaybackBuffering(index: Int) {
                    Log.i(TAG, "onPlaybackBuffering: index=$index")
                    // Called when the playback is buffering for the media at [index]
                }

                override fun onPlaybackEnd(index: Int) {
                    Log.i(TAG, "onPlaybackEnd: index=$index")
                    // Called when the playback is ended for the media at [index]
                }

                override fun onError(error: ErrorEvent): Boolean {
                    Log.i(TAG, "onError: error=$error")
                    // Called when the following errors occur:
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
                    Log.i(TAG, "onVideoEvent: videoEvent=$videoEvent")
                    // Called when the following video events occur:
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

                override fun onVideoSizeChanged(width: Int, height: Int) {
                    Log.i(TAG, "onVideoSizeChanged: width=$width, height=$height")
                    // Called when current video size changed
                }
            }
        )
    }

    companion object {
        const val TAG = "PlayActivity"
    }
}