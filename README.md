# BlendVision Loom Player

BlendVisionLoomPlayer is a Kotlin library to help you integrate BlendVision Loom player into your Android app.

## Requirement

The SDK supports
- Kotlin
- Android 6 or above


## Installation

### AAR

- Copy the following files required to the `lib` folder in your project:

```
blendvisionloomplayer-(latest_version).aar
kksplayer-library-ui-(kksplayer_version).aar
kksplayer-library-hls-(kksplayer_version).aar
kksplayer-library-dash-(kksplayer_version).aar
kksplayer-library-core-(kksplayer_version).aar
kksplayer-kkdrm-(kksplayer_version).aar
kksplayer-(kksplayer_version).aar
kks-paas-release-(kks_paas_version).aar
kks-network-(kks_network_version).aar
kks-daas-release-(kks_daas_version).aar
```

### Gradle Setting

- Add the following permission to `AndroidManifest.xml` file:
```xml
    <uses-permission android:name="android.permission.INTERNET" />
```

- Add the following plugins to the module-level `build.gradle` file:
```groovy
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-kapt'
}
```

- Add the following dependencies to the module-level `build.gradle` file:
```groovy
dependencies {
    // The libraries for BlendVision Loom Player
    implementation fileTree(dir: 'libs', include: ['*.jar', "*.aar"])

    // The required dependencies
    implementation "androidx.core:core-ktx:$core_ktx_version"
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
    implementation "com.google.code.gson:gson:$gson_version"
    implementation "com.google.android.gms:play-services-cast-framework:$cast_version"
    implementation "com.squareup.okhttp3:okhttp:$okHttp_version"
    implementation "com.google.ads.interactivemedia.v3:interactivemedia:$google_ima_version"
    implementation "com.github.bumptech.glide:glide:$glide_version"
    kapt "com.github.bumptech.glide:compiler:$glide_version"
    implementation "io.reactivex.rxjava2:rxjava:$rx_java_version"
    implementation "io.reactivex.rxjava2:rxandroid:$rx_android_version"
    implementation "io.reactivex.rxjava2:rxkotlin:$rx_kotlin_version"
    implementation "androidx.room:room-runtime:$androidx_room_version"
    kapt "androidx.room:room-compiler:$androidx_room_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_viewmodel_ktx"

    // To support com/google/common/collect/Sets
    api ('com.google.guava:guava:' + guavaVersion) {
        exclude group: 'com.google.code.findbugs', module: 'jsr305'
        exclude group: 'org.checkerframework', module: 'checker-compat-qual'
        exclude group: 'com.google.errorprone', module: 'error_prone_annotations'
        exclude group: 'com.google.j2objc', module: 'j2objc-annotations'
        exclude group: 'org.codehaus.mojo', module: 'animal-sniffer-annotations'
    }
}
```

- Add the following package option to the module-level `build.gradle` file:
```groovy
android {
    ...
    packagingOptions {
        exclude 'META-INF/*.kotlin_module'
    }
}
```

- Turn on Java 8 support in the module-level `build.gradle` file:

```groovy
android {
    ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = '1.8'
    }
}
```

- Enable view binding in the module-level `build.gradle` file:

```groovy
android {
    ...
    viewBinding {
        enabled = true
    }
}
```
## Usage

You can create a `BlendVisionLoomPlayer` instance using `BlendVisionLoomPlayer.presentPlayer`.

```kotlin
// Prepare the media items to be played
val mediaItems: List<MediaItem> = listOf()
// Find the container (FrameLayout, RelativeLayout or other ViewGroup) to attach the player
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
            // Called when the player is setup and the controller is available
        }

        override fun onPlaybackReady(index: Int) {
           // Called when the playback is able to immediately play from its current position for the media at [index]
        }

        override fun onPlaybackReady(index: Int, playWhenReady: Boolean) {
           // Called when the playback is able to immediately play from its current position with
           // the playWhenReady flag to indicate whether the playback will proceed for the media at [index]
        }

        override fun onPlaybackEnd(index: Int) {
            // Called when the playback is ended for the media at [index]
        }

        override fun onError(error: ErrorEvent): Boolean {
            // Called when an error occurs.
        }

    }
)
```

### Media items

In BlendVisionLoomPlayer every piece of media is represented by a `MediaItem`. To play a piece of media you need to build a corresponding `MediaItem` and add it to the player.
It's possible to setup the player with multiple media items to be played one after the other:

```kotlin
val firstItem = MediaItem(
    contentId = "CONTENT_ID_OF_FIRST_ITEM",
    title = "TITLE_OF_FIRST_ITEM",
    drm = MediaItem.DrmConfiguration(
        licenseUrl = "DRM_LICENSE_URL",
        headers = mapOf(
            "Authorization" to "Bearer $JWT"
            ... // other customized headers for DRM license
        )
    ),
    sources = listOf(
        MediaItem.Source(
            thumbnailSeekingUrl = "https://server/path/id.vtt",
            manifests = listOf(
                MediaItem.createDash(
                    url = "https://server/path/id.mpd"
                ),
                MediaItem.createHls(
                    url = "https://server/path/id.m3u8"
                )
            )
        )
    )
)

val secondItem = MediaItem(
    contentId = "CONTENT_ID_OF_SECOND_ITEM",
    title = "TITLE_OF_SECOND_ITEM",
    startTimeMs = 10000,
    drm = MediaItem.DrmConfiguration(
        licenseUrl = "DRM_LICENSE_URL",
        headers = mapOf(
            "Authorization" to "Bearer $JWT"
            ... // other customized headers for DRM license
        )
    ),
    sources = listOf(
        MediaItem.Source(
            thumbnailSeekingUrl = "https://server/path/id.vtt",
            manifests = listOf(
                MediaItem.createDash(
                    url = "https://server/path/id.mpd"
                ),
                MediaItem.createHls(
                    url = "https://server/path/id.m3u8"
                )
            )
        )
    )
)

BlendVisionLoomPlayer.presentPlayer(
    mediaItems = listOf(firstItem, secondItem)
)
```

You can specify the start position in milliseconds to start play from for each media item.

#### Media source

In BlendVisionLoomPlayer each media source contains two properties:
- thumbnailSeekingUrl: images generated for the ingested video
- manifests: available sources of the ingested video

```kotlin
MediaItem.Source(
    thumbnailSeekingUrl = "https://server/path/id.vtt",
    manifests = listOf(
        MediaItem.createDash(
            url = "https://server/path/id.mpd"
        ),
        MediaItem.createHls(
            url = "https://server/path/id.m3u8"
        )
    )
)
```

#### Media manifest

Internally BlendVisionLoomPlayer needs `Manifest` in `MediaItem` to play the content.

```kotlin
MediaItem.createDash(
    url = "https://server/path/id.mpd"
)
```

Currently we have two different source types: `DASH` and `HLS`. To create a manifest, the source url and supported resolutions must be provided.

### Protected content

For protected content, the media items' DRM properties should be set based on the following architecture:


```kotlin
val JWT: String = ...
...
MediaItem.DrmConfiguration(
    licenseUrl = "DRM_LICENSE_URL",
    headers = mapOf("Authorization" to "Bearer $JWT")
)
```

#### No protection

For no protection content, just skip the `drm` parameter or set it as `null`.

```kotlin
MediaItem(
    ...
    drm = null,
)
```

### Menu Item

For menu items displayed on BlendVisionLoomPlayer, the setting could be:

```kotlin
val barItems: List<MenuBarItem> = listOf(MenuBarItem.SETTING)
...
BlendVisionLoomPlayer.presentPlayer(
    playerContext = PlayerContext(
        activity = this,
        container = binding.container,
        barItems = barItems
    ),
    ...
)
```
Currently we support the type of menu item, `MenuBarItem.SETTING`. Note that you should ensure that each item added in `barItems` is unique.

#### Setting item
If you only want to add `MenuBarItem.SETTING`, you don't need to provide `barItems`, because the default argument of `barItems` adds only `MenuBarItem.SETTING`.

```kotlin
BlendVisionLoomPlayer.presentPlayer(
    playerContext = PlayerContext(
        activity = this,
        container = binding.container,
        barItems = listOf(MenuBarItem.SETTING) // or don't provide barItems
    ),
    ...
)
```
#### No items
If you don't need any menu items, just set `barItems` as `MenuBarItem.NO_ITEMS`
```kotlin
BlendVisionLoomPlayer.presentPlayer(
    playerContext = PlayerContext(
        activity = this,
        container = binding.container,
        barItems = MenuBarItem.NO_ITEMS
    ),
    ...
)
```

## Controlling the player

Once the player has been prepared, playback can be controlled by calling methods on the BlendVisionPlayerController. The supported methods are listed below:

- `play` and `pause` start and pause playback.
- `isPlaying` indicates whether the current playback is playing
- `previous`, `next`, `hasPrevious` and `hasNext` allow navigating through the playlist.
- `rewind` and `forward` rewind and fast-forward in the media (10 seconds).
- `showController` controls the visibility of the controller icons and progress bar.
- `seek` allows seeking within the media (milliseconds).
- `setBackgroundPlay` controls background playback.
- `enterFullScreen` enables the fullscreen mode of playback with auto-rotate setting.
- `exitFullScreen` disables the fullscreen mode of playback.
- `isFullscreen` indicates whether the fullscreen mode or not
- `replay` replay playback.
- `mute` and `unmute` mute and unmute audio in the media
- `release` release the underlying player 
- `restart` re-initialize the underlying player and restart playback
- `cancelPreCacheAndPlay` cancel pre-caching and start playback
- `cancelPreCache` cancel pre-caching
- `getPosition` return the playback position in the current content, in milliseconds
- `getBuffered` return an estimated of the position in the current content up to which data is buffered, in milliseconds
- `getDuration` return the duration of the current content, in milliseconds


## Handling the error

Once an error occurs, the method `onError` in EventListener is called. This method must return a boolean to indicate whether an error screen is shown. That is, return `true` to indicate that you have handled the event; return `false` if you have not handled it, and the error message displays on the built-in error view.

The error events are listed below:

- BasicNetworkError
- ServerResponseError
- PlayerError
- PaaSInternalError
- StartIndexOutOfRangeError
- EmptyMediaItemError
- UndefinedError

## Handling the video event

It is possible to listen to the video event, like video playback began, video playback ended, play button click, for video tracking or analysis.

The code snippet below shows how to listen the video event. Once a video event occurs, the method `onVideoEvent` in EventListener is called.

```kotlin
BlendVisionLoomPlayer.presentPlayer(
    ...
    eventListener = object : EventListener {
        ...
        // Handel the video events
        fun onVideoEvent(videoEvent: VideoEvent) {
            when (videoEvent) {
                is VideoEvent.VideoPlaybackBegan -> {
                    // Get the index and position of the video
                    val index = videoEvent.contentIndex
                    val position = videoEvent.position
                    ...
                }
                is VideoEvent.VideoPlaybackEnded -> {
                    // Get the index and position of the video
                    val index = videoEvent.contentIndex
                    val position = videoEvent.position
                    ...
                }
                is VideoEvent.VideoPlaybackStopped -> {
                    // Get the index and played duration of the video
                    val index = videoEvent.contentIndex
                    val playedDuration = videoEvent.playedDuration
                    ...
                }
                is VideoEvent.VideoPlaybackErrorOccurred -> {
                    // Get the index, position and error of the video
                    val index = videoEvent.contentIndex
                    val position = videoEvent.position
                    var error = videoEvent.error
                    ...
                }
                is VideoEvent.Play -> {
                    // Get the index and position of the video
                    val index = videoEvent.contentIndex
                    val position = videoEvent.position
                    ...
                }
                is VideoEvent.Pause -> {
                    // Get the index and position of the video
                    val index = videoEvent.contentIndex
                    val position = videoEvent.position
                    ...
                }
                is VideoEvent.Rewind -> {
                    // Get the index and position of the video
                    val index = videoEvent.contentIndex
                    val position = videoEvent.position
                    ...
                }
                is VideoEvent.Forward -> {
                    // Get the index and position of the video
                    val index = videoEvent.contentIndex
                    val position = videoEvent.position
                    ...
                }
                is VideoEvent.PreviousEpisode -> {
                    // Get the index and position of the video
                    val index = videoEvent.contentIndex
                    val position = videoEvent.position
                    ...
                }
                is VideoEvent.NextEpisode -> {
                    // Get the index and position of the video
                    val index = videoEvent.contentIndex
                    val position = videoEvent.position
                    ...
                }
                is VideoEvent.VideoSeekingEnded -> {
                    // Get the index, seek from/to of the video
                    val index = videoEvent.contentIndex
                    val seekFrom = videoEvent.seekFrom
                    val seekTo = videoEvent.seekTo
                    ...
                }
                is VideoEvent.SettingPageEntered -> {
                    // Get the index of the video
                    val index = videoEvent.contentIndex
                    ...
                }
                is VideoEvent.SettingPageExited ->{
                    // Get the index of the video
                    val index = videoEvent.contentIndex
                    ...
                }
                else -> {
                }
            }
        }
    }
)
```


The supported video events are listed below:

- `VideoPlaybackBegan` which is triggered when the media has began.
- `VideoPlaybackEnded` which is triggered when the media has ended.
- `VideoPlaybackStopped` which is triggered when the media is stopped.
- `VideoPlaybackErrorOccurred` which is triggered when an error occurs.
- `Play` which is triggered when the media is played.
- `Pause` which is triggered when the media is paused.
- `Rewind` which is triggered when the media is rewound.
- `Forward` which is triggered when the media is forwarded.
- `NextEpisode` which is triggered when the media is skipped to the next episode.
- `PreviousEpisode` which is triggered when the media is skipped to the previous episode.
- `VideoSeekingEnded` which is triggered when the media has been seeking ended.
- `SettingPageEntered` which is triggered when the setting page has been entered.
- `SettingPageExited` which is triggered when the setting page has been exited.

## Pre-Caching
The BlendVisionLoomPlayer supports pre-caching the streaming before playing playback. The code snippet below shows how to enable pre-caching function and how to cancel the caching then to play.

Note that the maximum size of the cache is 100 MB and the caching mechanism will evict the least recently used cache files first when the cache is full.

**Enable pre-caching**
```kotlin
BlendVisionLoomPlayer.presentPlayer(
    playerContext = PlayerContext(
        ...
        configuration = Configuration(isPreCacheEnable = true)
    ),
    ...
)
```

**Cancel pre-caching and play**
```kotlin
private var controller: BlendVisionPlayerController? = null
...
controller?.cancelPreCacheAndPlay()
```

**Cancel pre-caching**
```kotlin
private var controller: BlendVisionPlayerController? = null
...
controller?.cancelPreCache()
```

## Set maximum buffer duration
It is possible to adjust the maximum duration of a playback that the player will attempt to buffer, in milliseconds. 
We call the value as `maxBufferMs`. The default maximum is 50000ms, and the lower limit of `maxBufferMs` is 15000ms. 
Any value of `maxBufferMs` set to less than 15000ms will be set to 15000ms.

Note that the buffering duration may be affected by bandwidth, memory, device capability and may not reach to the maximum during playback duration.

The code snippet below show how to set `maxBufferMs`.
```kotlin
BlendVisionLoomPlayer.presentPlayer(
    playerContext = PlayerContext(
        ...
        configuration = Configuration(maxBufferMs = 30000)
    ),
    ...
)
```


## ProGuard
Depending on your ProGuard (DexGuard) config and usage, you may need to include the following lines in your `proguard-rules.pro` :

```pro
-keep interface com.kkstream.blendvisionloom.** { *; }

-keep public class com.kkstream.blendvisionloom.** { *; }
```
