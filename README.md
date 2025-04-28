# Android Kotlin 常用工具类库
## 这是一个收集和实现 Android 开发中常用工具类的 Kotlin 开源项目。旨在提供一系列便捷、高效的工具类，帮助开发者提高开发效率，减少重复代码。

### 项目概览
本项目包含了以下常用工具类：

[base基类](https://github.com/chengzeli7/Android_Utils/tree/main/base): BaseActivity、BaseFragment、BaseDialog、BaseView等

[ActivityUtils](https://github.com/chengzeli7/Android_Utils/blob/main/ActivityUtils.kt): Toast 消息管理

[NetworkUtils](https://github.com/chengzeli7/Android_Utils/blob/main/NetworkUtils.kt): 网络状态判断

[ScreenUtils](https://github.com/chengzeli7/Android_Utils/blob/main/ScreenUtils.kt): 屏幕相关信息及单位转换

[FileUtils](https://github.com/chengzeli7/Android_Utils/blob/main/FileUtils.kt): 文件操作

[SPUtils](https://github.com/chengzeli7/Android_Utils/blob/main/SPUtils.kt): SharedPreferences 简化操作

[ActivityUtils](https://github.com/chengzeli7/Android_Utils/blob/main/ActivityUtils.kt): Activity 管理及跳转

[KeyboardUtils](https://github.com/chengzeli7/Android_Utils/blob/main/KeyboardUtils.kt): 软键盘控制

[TimeUtils](https://github.com/chengzeli7/Android_Utils/blob/main/TimeUtils.kt): 时间和日期处理

[EncryptUtils](https://github.com/chengzeli7/Android_Utils/blob/main/EncryptUtils.kt): 加密解密

[RegexUtils](https://github.com/chengzeli7/Android_Utils/blob/main/RegexUtils.kt): 正则表达式匹配

[DeviceUtils](https://github.com/chengzeli7/Android_Utils/blob/main/DeviceUtils.kt): 设备信息获取

[ResourceUtils](https://github.com/chengzeli7/Android_Utils/blob/main/ResourceUtils.kt): 资源获取

[PermissionUtils](https://github.com/chengzeli7/Android_Utils/blob/main/PermissionUtils.kt): 运行时权限处理

[AppUtils](https://github.com/chengzeli7/Android_Utils/blob/main/AppUtils.kt): 应用信息及操作

[ClipboardUtils](https://github.com/chengzeli7/Android_Utils/blob/main/ClipboardUtils.kt): 剪贴板操作

[VibrateUtils](https://github.com/chengzeli7/Android_Utils/blob/main/VibrateUtils.kt): 设备震动控制

[LogUtils](https://github.com/chengzeli7/Android_Utils/blob/main/LogUtils.kt): 增强型日志记录

[IntentUtils](https://github.com/chengzeli7/Android_Utils/blob/main/IntentUtils.kt): Intent 常用操作

[ViewUtils](https://github.com/chengzeli7/Android_Utils/blob/main/ViewUtils.kt): View 相关操作

[AnimationUtils](https://github.com/chengzeli7/Android_Utils/blob/main/AnimationUtils.kt): 常用动画效果

[ServiceUtils](https://github.com/chengzeli7/Android_Utils/blob/main/ServiceUtils.kt): Service 启动与停止

[BroadcastReceiverUtils](https://github.com/chengzeli7/Android_Utils/blob/main/BroadcastReceiverUtils.kt): 广播接收器注册与发送

[AudioUtils](https://github.com/chengzeli7/Android_Utils/blob/main/AudioUtils.kt): 音频播放与录制

[CrashHandlerUtils](https://github.com/chengzeli7/Android_Utils/blob/main/CrashHandlerUtils.kt): 应用崩溃捕获与处理

### 如何使用
将所需的工具类文件复制到你的 Android 项目中。

根据每个工具类的说明，在 AndroidManifest.xml 中声明必要的权限。

对于需要初始化的工具类（如 Toaster, SPUtils, CrashHandlerUtils），在你的 Application 的 onCreate() 方法中进行初始化。

在你的代码中直接调用工具类的方法。

[使用方式](https://github.com/chengzeli7/Android_Utils/blob/main/How%20to%20use%20it.md)

### 贡献
欢迎贡献代码，如果你有任何好的想法或实现了新的工具类，请提交 Pull Request。
