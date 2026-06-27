
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.5.0'
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

# 媒体播放器 - Android 4.1 兼容版

## 功能
- 在线视频播放（MP4 / RTSP / M3U8 直播流）
- 在线音乐播放（MP3 / AAC / OGG）
- 支持进度拖动、快进 / 快退 10 秒
- 视频自带系统播放控制条
- 无任何第三方依赖，纯系统 API

## 支持系统
- 最低 Android 4.1（API 16）
- 目标 Android 4.4（API 19）
- 兼容 HTC One SC / SV 等机型

## 编译方法

### 方法一：Android Studio（推荐）
1. 安装 Android Studio（任意版本均可）
2. 打开本项目文件夹
3. 等待 Gradle 同步完成
4. 点击菜单 Build → Build APK
5. APK 生成在 `app/build/outputs/apk/` 目录

### 方法二：命令行
```bash
# 需要安装 Android SDK 和 JDK 7+
./gradlew assembleDebug
```

## 项目结构
```
MediaPlayer/
├── app/src/main/
│   ├── AndroidManifest.xml         权限声明
│   ├── java/com/example/mediaplayer/
│   │   ├── MainActivity.java       主界面（视频/音乐分Tab）
│   │   ├── VideoPlayerActivity.java 视频播放器
│   │   └── MusicPlayerActivity.java 音乐播放器
│   └── res/
│       ├── layout/                 界面布局文件
│       └── values/                 字符串和主题
├── build.gradle
└── settings.gradle
```

## 注意事项
- Android 4.1 不支持 HTTPS，若服务器仅提供 HTTPS 地址需要降级为 HTTP 或升级系统
- M3U8 直播流需要服务器支持 HTTP Live Streaming
- RTSP 流媒体视频稳定性取决于网络和设备解码能力

## 可播放的格式
| 类型 | 格式 |
|------|------|
| 视频 | MP4 (H.264), 3GP, WebM, RTSP |
| 音频 | MP3, AAC, OGG, WAV, FLAC |
| 直播 | M3U8 (HLS), RTSP |

include ':app'
