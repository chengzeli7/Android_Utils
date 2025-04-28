Android Kotlin 常用工具类库
这是一个收集和实现 Android 开发中常用工具类的 Kotlin 开源项目。旨在提供一系列便捷、高效的工具类，帮助开发者提高开发效率，减少重复代码。

项目概览
本项目包含了以下常用工具类：

Toaster: Toast 消息管理

NetworkUtils: 网络状态判断

ScreenUtils: 屏幕相关信息及单位转换

FileUtils: 文件操作

SPUtils: SharedPreferences 简化操作

ActivityUtils: Activity 管理及跳转

KeyboardUtils: 软键盘控制

TimeUtils: 时间和日期处理

EncryptUtils: 加密解密

RegexUtils: 正则表达式匹配

DeviceUtils: 设备信息获取

ResourceUtils: 资源获取

PermissionUtils: 运行时权限处理

AppUtils: 应用信息及操作

ClipboardUtils: 剪贴板操作

VibrateUtils: 设备震动控制

SnackbarUtils: Snackbar 显示

LogUtils: 增强型日志记录

IntentUtils: Intent 常用操作

ViewUtils: View 相关操作

AnimationUtils: 常用动画效果

ServiceUtils: Service 启动与停止

BroadcastReceiverUtils: 广播接收器注册与发送

AudioUtils: 音频播放与录制

LocationUtils: 位置信息获取

CrashHandlerUtils: 应用崩溃捕获与处理

如何使用
将所需的工具类文件复制到你的 Android 项目中。

根据每个工具类的说明，在 AndroidManifest.xml 中声明必要的权限。

对于需要初始化的工具类（如 Toaster, SPUtils, CrashHandlerUtils），在你的 Application 的 onCreate() 方法中进行初始化。

在你的代码中直接调用工具类的方法。

贡献
欢迎贡献代码，如果你有任何好的想法或实现了新的工具类，请提交 Pull Request。

许可证
本项目使用 MIT 许可证 开源。
