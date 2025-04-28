package com.miaoyin.weiqi.other.utlis

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi

/**
 * 通知工具类
 * 用于创建和管理通知
 */
object NotificationUtils {

    private const val TAG = "NotificationUtils"
    private const val DEFAULT_CHANNEL_ID = "default_channel" // 默认通知渠道 ID
    private const val DEFAULT_CHANNEL_NAME = "Default Channel" // 默认通知渠道名称

    /**
     * 创建通知渠道 (Android O 及以上版本需要)
     * @param context Context 对象
     * @param channelId 通知渠道 ID
     * @param channelName 通知渠道名称
     * @param importance 通知渠道重要性 (NotificationManager.IMPORTANCE_*)
     * @param description 通知渠道描述 (可选)
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: CharSequence,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        description: String? = null
    ) {
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description?.let { this.description = it }
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        Log.d(TAG, "Notification channel created: $channelId")
    }

    /**
     * 显示简单的通知
     * @param context Context 对象
     * @param notificationId 通知 ID
     * @param title 通知标题
     * @param content 通知内容
     * @param smallIconResId 小图标资源 ID
     * @param channelId 通知渠道 ID (Android O 及以上版本需要，默认为 DEFAULT_CHANNEL_ID)
     * @param pendingIntent 点击通知后执行的 PendingIntent (可选)
     */
    fun showBasicNotification(
        context: Context,
        notificationId: Int,
        title: CharSequence?,
        content: CharSequence?,
        @DrawableRes smallIconResId: Int,
        channelId: String = DEFAULT_CHANNEL_ID,
        pendingIntent: PendingIntent? = null
    ) {
        // Android O 及以上版本需要创建通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 设置通知优先级
            .setAutoCancel(true) // 点击通知后自动移除

        pendingIntent?.let { builder.setContentIntent(it) }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
        Log.d(TAG, "Showing basic notification with ID: $notificationId")
    }

    /**
     * 显示带有大图片的通知
     * @param context Context 对象
     * @param notificationId 通知 ID
     * @param title 通知标题
     * @param content 通知内容
     * @param smallIconResId 小图标资源 ID
     * @param largeIconBitmap 大图标 Bitmap (可选)
     * @param bigPictureBitmap 大图片 Bitmap
     * @param channelId 通知渠道 ID (Android O 及以上版本需要，默认为 DEFAULT_CHANNEL_ID)
     * @param pendingIntent 点击通知后执行的 PendingIntent (可选)
     */
    fun showBigPictureNotification(
        context: Context,
        notificationId: Int,
        title: CharSequence?,
        content: CharSequence?,
        @DrawableRes smallIconResId: Int,
        largeIconBitmap: Bitmap? = null,
        bigPictureBitmap: Bitmap,
        channelId: String = DEFAULT_CHANNEL_ID,
        pendingIntent: PendingIntent? = null
    ) {
        // Android O 及以上版本需要创建通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME)
        }

        val style = NotificationCompat.BigPictureStyle()
            .bigPicture(bigPictureBitmap)
            .setBigContentTitle(title) // 大图模式下的标题
            .setSummaryText(content) // 大图模式下的内容摘要

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setStyle(style)

        largeIconBitmap?.let { builder.setLargeIcon(it) }
        pendingIntent?.let { builder.setContentIntent(it) }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
        Log.d(TAG, "Showing big picture notification with ID: $notificationId")
    }

    /**
     * 取消指定 ID 的通知
     * @param context Context 对象
     * @param notificationId 要取消的通知 ID
     */
    fun cancelNotification(context: Context, notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
        Log.d(TAG, "Cancelled notification with ID: $notificationId")
    }

    /**
     * 取消所有通知
     * @param context Context 对象
     */
    fun cancelAllNotifications(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        Log.d(TAG, "Cancelled all notifications.")
    }

    // TODO: 添加不同类型的通知样式 (例如 InboxStyle, MessagingStyle)
    // TODO: 添加通知组 (Notification Groups)
    // TODO: 添加通知的 Action 按钮
    // TODO: 考虑通知的优先级和重要性设置
}
