package com.lib.base

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

// Toast 通用类

object Toaster {
    private const val TAG = "Toaster" // Tag for logging
    private var application: Application? = null
    private var toast: Toast? = null // Add a toast instance to cancel previous ones

    /**
     * 初始化 Toaster 工具类
     * @param app Application 实例
     */
    fun init(app: Application) {
        application = app
        Log.d(TAG, "Toaster initialized.")
    }

    /**
     * 显示 Toast 消息
     * @param message 要显示的文本消息
     * @param duration Toast 显示时长，默认为 Toast.LENGTH_SHORT
     */
    fun show(message: String, duration: Int = Toast.LENGTH_SHORT) {
        application?.let { context ->
            // Ensure Toast is shown on the main thread
            Handler(Looper.getMainLooper()).post {
                // 取消上一个 Toast，避免 Toast 累积显示
                toast?.cancel()
                toast = Toast.makeText(context, message, duration)
                toast?.show()
            }
        } ?: run {
            Log.e(TAG, "Toaster not initialized. Call init() first.")
            // Optionally, you could still show a basic log message if not initialized
            // Log.e(TAG, "Failed to show toast: $message")
            throw IllegalStateException("Toaster not initialized. Call init() first.")
        }
    }

    /**
     * 显示 Toast 消息
     * @param resId 要显示的字符串资源的 ID
     * @param duration Toast 显示时长，默认为 Toast.LENGTH_SHORT
     */
    fun show(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
        application?.let { context ->
            // Ensure Toast is shown on the main thread
            Handler(Looper.getMainLooper()).post {
                // 取消上一个 Toast，避免 Toast 累积显示
                toast?.cancel()
                toast = Toast.makeText(context, resId, duration)
                toast?.show()
            }
        } ?: run {
            Log.e(TAG, "Toaster not initialized. Call init() first.")
            // Optionally, you could still show a basic log message if not initialized
            // Log.e(TAG, "Failed to show toast with resource ID: $resId")
            throw IllegalStateException("Toaster not initialized. Call init() first.")
        }
    }

    /**
     * 显示 Toast 消息
     * @param text 要显示的 CharSequence 文本
     * @param duration Toast 显示时长，默认为 Toast.LENGTH_SHORT
     */
    fun show(text: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
        application?.let { context ->
            // Ensure Toast is shown on the main thread
            Handler(Looper.getMainLooper()).post {
                // 取消上一个 Toast，避免 Toast 累积显示
                toast?.cancel()
                // Make sure text is not null before showing
                if (text != null) {
                    toast = Toast.makeText(context, text, duration)
                    toast?.show()
                } else {
                    Log.w(TAG, "Attempted to show toast with null CharSequence.")
                }
            }
        } ?: run {
            Log.e(TAG, "Toaster not initialized. Call init() first.")
            // Optionally, you could still show a basic log message if not initialized
            // Log.e(TAG, "Failed to show toast with CharSequence: $text")
            throw IllegalStateException("Toaster not initialized. Call init() first.")
        }
    }

    /**
     * 显示 Toast 消息
     * @param any 可以是任意对象，将调用 toString() 方法转换为文本显示
     * @param duration Toast 显示时长，默认为 Toast.LENGTH_SHORT
     */
    fun show(any: Any?, duration: Int = Toast.LENGTH_SHORT) {
        application?.let { context ->
            // Ensure Toast is shown on the main thread
            Handler(Looper.getMainLooper()).post {
                // 取消上一个 Toast，避免 Toast 累积显示
                toast?.cancel()
                if (any != null) {
                    toast = Toast.makeText(context, any.toString(), duration)
                    toast?.show()
                } else {
                    Log.w(TAG, "Attempted to show toast with null Any object.")
                }
            }
        } ?: run {
            Log.e(TAG, "Toaster not initialized. Call init() first.")
            // Optionally, you could still show a basic log message if not initialized
            // Log.e(TAG, "Failed to show toast with Any object: $any")
            throw IllegalStateException("Toaster not initialized. Call init() first.")
        }
    }

    /**
     * 取消当前正在显示的 Toast
     */
    fun cancelAll() {
        Handler(Looper.getMainLooper()).post {
            toast?.cancel()
            Log.d(TAG, "Current toast cancelled.")
        }
    }
}
