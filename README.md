# BlendVision Loom Player

BlendVisionLoomPlayer is a Kotlin library to help you integrate BlendVision Loom player into your Android app.

## Requirement

The SDK supports
- Kotlin
- Android 6 or above


## Installation

### AAR

- Copy the following files required in the `sdk` folder to the `lib` folder in your project:

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
            // Control player using BlendVisionPlayerController
        }

        override fun onPlaybackEnd(index: Int) {
            // logic when playback END for the media at [index]
        }

        override fun onError(error: ErrorEvent): Boolean {
            // Handle the error
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
