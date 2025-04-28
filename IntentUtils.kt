package com.miaoyin.weiqi.other.utlis.new

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log

/**
 * Intent 工具类
 * 简化常见的 Intent 操作
 */
object IntentUtils {

    private const val TAG = "IntentUtils"

    /**
     * 跳转到应用设置页面
     * @param context Context 对象
     */
    fun goToAppSetting(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
            Log.d(TAG, "Navigating to app settings.")
        } catch (e: Exception) {
            Log.e(TAG, "Error navigating to app settings", e)
        }
    }

    /**
     * 拨打电话
     * 需要权限: <uses-permission android:name="android.permission.CALL_PHONE" />
     * @param context Context 对象
     * @param phoneNumber 电话号码
     */
    fun dialPhoneNumber(context: Context, phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            context.startActivity(intent)
            Log.d(TAG, "Dialing phone number: $phoneNumber")
        } catch (e: Exception) {
            Log.e(TAG, "Error dialing phone number: $phoneNumber", e)
        }
    }

    /**
     * 直接拨打电话 (需要 CALL_PHONE 权限)
     * 注意：此操作会直接拨出电话，请谨慎使用并确保已获得用户授权。
     * @param context Context 对象
     * @param phoneNumber 电话号码
     */
    // @RequiresPermission(android.Manifest.permission.CALL_PHONE) // Requires explicit permission check before calling
    fun callPhoneNumber(context: Context, phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")
            context.startActivity(intent)
            Log.d(TAG, "Calling phone number: $phoneNumber")
        } catch (e: Exception) {
            Log.e(TAG, "Error calling phone number: $phoneNumber", e)
        }
    }

    /**
     * 发送短信
     * @param context Context 对象
     * @param phoneNumber 接收短信的电话号码 (可选)
     * @param message 短信内容 (可选)
     */
    fun sendSms(context: Context, phoneNumber: String? = null, message: String? = null) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("smsto:${phoneNumber ?: ""}")
            message?.let { intent.putExtra("sms_body", it) }
            context.startActivity(intent)
            Log.d(TAG, "Sending SMS to: $phoneNumber with message: $message")
        } catch (e: Exception) {
            Log.e(TAG, "Error sending SMS", e)
        }
    }

    /**
     * 打开网页
     * @param context Context 对象
     * @param url 网页 URL
     */
    fun openWebPage(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
            Log.d(TAG, "Opening web page: $url")
        } catch (e: Exception) {
            Log.e(TAG, "Error opening web page: $url", e)
        }
    }

    /**
     * 发送邮件
     * @param context Context 对象
     * @param recipients 接收者邮箱地址数组
     * @param subject 邮件主题 (可选)
     * @param body 邮件内容 (可选)
     */
    fun sendEmail(
        context: Context, recipients: Array<String>, subject: String? = null, body: String? = null
    ) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:") // Only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            subject?.let { intent.putExtra(Intent.EXTRA_SUBJECT, it) }
            body?.let { intent.putExtra(Intent.EXTRA_TEXT, it) }
            context.startActivity(intent)
            Log.d(TAG, "Sending email to: ${recipients.joinToString()}")
        } catch (e: Exception) {
            Log.e(TAG, "Error sending email", e)
        }
    }

    /**
     * 分享文本
     * @param context Context 对象
     * @param text 要分享的文本
     * @param title 分享对话框的标题 (可选)
     */
    fun shareText(context: Context, text: String, title: String? = null) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            context.startActivity(Intent.createChooser(intent, title))
            Log.d(TAG, "Sharing text: $text")
        } catch (e: Exception) {
            Log.e(TAG, "Error sharing text", e)
        }
    }

    /**
     * 分享文件
     * @param context Context 对象
     * @param uri 文件 Uri
     * @param mimeType 文件的 MIME 类型 (例如 "image/jpeg", "application/pdf")
     * @param title 分享对话框的标题 (可选)
     */
    fun shareFile(context: Context, uri: Uri, mimeType: String, title: String? = null) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = mimeType
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant read permission to the receiving app
            context.startActivity(Intent.createChooser(intent, title))
            Log.d(TAG, "Sharing file: $uri with MIME type: $mimeType")
        } catch (e: Exception) {
            Log.e(TAG, "Error sharing file: $uri", e)
        }
    }


    // TODO: 添加跳转到其他应用特定页面的方法 (需要知道对方应用的 ComponentName)
    // TODO: 添加打开文件的方法 (需要根据文件类型使用不同的 Intent)
    // TODO: 考虑 Intent 的 Flags 设置
}
