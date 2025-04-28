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

工具类详情
Toaster
功能: 简化 Toast 消息的显示，避免重复 Toast 消息堆积。

主要方法:

init(app: Application): 初始化 Toaster。

show(message: String, duration: Int): 显示文本消息。

show(resId: Int, duration: Int): 显示字符串资源消息。

cancelAll(): 取消当前正在显示的 Toast。

NetworkUtils
功能: 判断设备的网络连接状态和网络类型。

主要方法:

isNetworkAvailable(context: Context): 检查网络是否可用。

isWifiConnected(context: Context): 判断是否连接 WiFi。

isMobileDataConnected(context: Context): 判断是否连接移动数据。

ScreenUtils
功能: 获取屏幕尺寸、密度等信息，并进行单位转换。

主要方法:

getScreenWidth(context: Context): 获取屏幕宽度（像素）。

getScreenHeight(context: Context): 获取屏幕高度（像素）。

dp2px(dpValue: Float): dp 转 px。

px2dp(pxValue: Float): px 转 dp。

sp2px(spValue: Float): sp 转 px。

px2sp(pxValue: Float): px 转 sp。

FileUtils
功能: 提供文件的创建、删除、复制、获取大小等操作。

主要方法:

createDir(dirPath: String): 创建目录。

createFile(filePath: String): 创建文件。

deleteFile(file: File?): 删除文件或目录。

copyFile(sourceFile: File, destFile: File): 复制文件。

getFileSize(file: File?): 获取文件大小。

formatFileSize(fileSize: Long): 格式化文件大小字符串。

SPUtils
功能: 简化 SharedPreferences 的存取操作。

主要方法:

init(context: Context, spName: String): 初始化 SPUtils。

putString(key: String, value: String?): 存储 String。

getString(key: String, defaultValue: String?): 获取 String。

支持存储和获取 Int, Boolean, Float, Long, Set 等类型。

remove(key: String): 移除指定键。

clear(): 清除所有数据。

ActivityUtils
功能: Activity 管理（入栈、出栈）、启动 Activity、检查 Activity 是否存在等。

主要方法:

pushActivity(activity: Activity): Activity 入栈。

popActivity(activity: Activity): Activity 出栈。

currentActivity(): 获取当前栈顶 Activity。

finishAllActivities(): 结束所有 Activity。

startActivity(context: Context, activityClass: Class<*>, bundle: Bundle?): 启动 Activity。

isActivityExists(context: Context, packageName: String, className: String): 检查 Activity 是否存在。

KeyboardUtils
功能: 控制软键盘的显示和隐藏。

主要方法:

showKeyboard(view: View): 显示软键盘。

hideKeyboard(view: View): 隐藏软键盘。

toggleKeyboard(context: Context): 切换软键盘状态。

TimeUtils
功能: 时间和日期的格式化、转换及计算。

主要方法:

formatTime(timeMillis: Long, format: String): 格式化时间戳。

parseTime(timeString: String, format: String): 解析时间字符串。

getCurrentTimeMillis(): 获取当前时间戳。

getTimeDifferenceMillis(time1: Long, time2: Long): 计算时间差。

EncryptUtils
功能: 提供常用的加密算法。

主要方法:

encryptMD5(data: String): MD5 加密。

encryptSHA256(data: String): SHA-256 加密。

RegexUtils
功能: 提供常用的正则表达式匹配方法。

主要方法:

isMatch(input: CharSequence?, regex: String): 检查是否匹配。

isMobileSimple(input: CharSequence?): 检查是否是手机号。

isEmail(input: CharSequence?): 检查是否是邮箱。

getMatches(input: CharSequence?, regex: String): 获取所有匹配项。

DeviceUtils
功能: 获取设备型号、系统版本、屏幕分辨率、内存信息等。

主要方法:

getDeviceModel(): 获取设备型号。

getSystemVersion(): 获取系统版本。

getScreenWidth(context: Context): 获取屏幕宽度。

getScreenHeight(context: Context): 获取屏幕高度。

getNumberOfCPUCores(): 获取 CPU 核心数。

getTotalMemory(context: Context): 获取总内存。

getAvailableMemory(context: Context): 获取可用内存。

ResourceUtils
功能: 方便获取颜色、字符串、Drawable 等资源。

主要方法:

getColor(context: Context, colorResId: Int): 获取颜色。

getString(context: Context, stringResId: Int): 获取字符串。

getDrawable(context: Context, drawableResId: Int): 获取 Drawable。

getDimension(context: Context, dimenResId: Int): 获取尺寸。

PermissionUtils
功能: 简化运行时权限的检查和请求。

主要方法:

hasPermissions(context: Context, vararg permissions: String): 检查是否拥有权限。

requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int): 请求权限。

handleRequestPermissionsResult(...): 处理权限请求结果。

AppUtils
功能: 获取应用版本信息、检查应用是否安装、启动其他应用等。

主要方法:

getAppVersionName(context: Context): 获取版本名称。

getAppVersionCode(context: Context): 获取版本号。

isAppInstalled(context: Context, packageName: String): 检查应用是否安装。

launchApp(context: Context, packageName: String): 启动其他应用。

goToAppMarket(context: Context, packageName: String): 跳转应用商店。

ClipboardUtils
功能: 简化剪贴板的复制和粘贴操作。

主要方法:

copyText(context: Context, text: CharSequence?): 复制文本。

pasteText(context: Context): 粘贴文本。

copyUri(context: Context, uri: Uri?): 复制 Uri。

pasteUri(context: Context): 粘贴 Uri。

VibrateUtils
功能: 控制设备的震动。

主要方法:

vibrate(context: Context, duration: Long): 震动指定时长。

vibrate(context: Context, pattern: LongArray, repeat: Int): 按模式震动。

cancel(context: Context): 取消震动。

SnackbarUtils
功能: 方便地显示 Snackbar，支持自定义样式。

主要方法:

showShort(view: View, text: CharSequence, ...): 显示短时 Snackbar。

showLong(view: View, text: CharSequence, ...): 显示长时 Snackbar。

showWithAction(view: View, text: CharSequence, actionText: CharSequence, listener: View.OnClickListener, ...): 显示带 Action 的 Snackbar。

LogUtils
功能: 增强型日志记录，支持分级过滤和文件写入（包括文件轮转和异步写入）。

主要方法:

setDebug(debug: Boolean): 设置是否为调试模式。

setMinLogLevel(level: Int): 设置最小输出日志级别。

enableFileLogging(filePath: String): 启用文件日志。

disableFileLogging(): 禁用文件日志。

v(...), d(...), i(...), w(...), e(...): 输出不同级别的日志。

IntentUtils
功能: 简化常见的 Intent 跳转操作。

主要方法:

goToAppSetting(context: Context): 跳转应用设置。

dialPhoneNumber(context: Context, phoneNumber: String): 拨打电话。

sendSms(context: Context, phoneNumber: String?, message: String?): 发送短信。

openWebPage(context: Context, url: String): 打开网页。

sendEmail(context: Context, recipients: Array<String>, ...): 发送邮件。

shareText(context: Context, text: String, title: String?): 分享文本。

shareFile(context: Context, uri: Uri, mimeType: String, title: String?): 分享文件。

ViewUtils
功能: 提供 View 的可见性控制、尺寸获取、测量和截图等操作。

主要方法:

setVisible(view: View), setInvisible(view: View), setGone(view: View): 设置可见性。

isVisible(view: View), isInvisible(view: View), isGone(view: View): 判断可见性。

getViewWidth(view: View), getViewHeight(view: View): 获取尺寸。

measureView(view: View, callback: (width: Int, height: Int) -> Unit): 测量 View。

getBitmapFromView(view: View): 获取 View 截图。

AnimationUtils
功能: 简化常用补间动画的创建和启动。

主要方法:

createAlphaAnimation(...): 创建透明度动画。

createScaleAnimation(...): 创建缩放动画。

createRotateAnimation(...): 创建旋转动画。

createTranslateAnimation(...): 创建平移动画。

createAnimationSet(...): 创建动画集合。

startAnimation(view: View, animation: Animation, listener: Animation.AnimationListener?): 启动动画。

ServiceUtils
功能: 方便启动和停止 Service。

主要方法:

startService(context: Context, serviceClass: Class<out Service>, extras: Bundle?): 启动 Service。

stopService(context: Context, serviceClass: Class<out Service>): 停止 Service。

BroadcastReceiverUtils
功能: 简化广播接收器的注册和注销，以及广播的发送。

主要方法:

registerLocalReceiver(context: Context, receiver: BroadcastReceiver, filter: IntentFilter): 注册本地广播。

registerGlobalReceiver(context: Context, receiver: BroadcastReceiver, filter: IntentFilter, ...): 注册全局广播。

unregisterReceiver(context: Context, receiver: BroadcastReceiver): 注销广播。

sendLocalBroadcast(context: Context, intent: Intent): 发送本地广播。

sendOrderedBroadcast(context: Context, intent: Intent, receiverPermission: String?): 发送有序广播。

AudioUtils
功能: 提供音频播放和录制功能。

主要方法:

playAudio(context: Context, filePath: String, ...): 播放音频文件。

playAudio(context: Context, uri: Uri, ...): 播放音频 Uri。

stopAudio(): 停止播放。

startRecording(filePath: String): 开始录制。

stopRecording(): 停止录制。

getAudioDuration(filePath: String): 获取音频时长。

CrashHandlerUtils
功能: 捕获应用未捕获的异常，收集设备信息并保存崩溃日志。

主要方法:

getInstance(): 获取单例实例。

init(context: Context): 初始化崩溃处理器。

如何使用
将所需的工具类文件复制到你的 Android 项目中。

根据每个工具类的说明，在 AndroidManifest.xml 中声明必要的权限。

对于需要初始化的工具类（如 Toaster, SPUtils, CrashHandlerUtils），在你的 Application 的 onCreate() 方法中进行初始化。

在你的代码中直接调用工具类的方法。

贡献
欢迎贡献代码，如果你有任何好的想法或实现了新的工具类，请提交 Pull Request。

许可证
本项目使用 MIT 许可证 开源。
