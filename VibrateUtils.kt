package com.miaoyin.weiqi.other.utlis

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

/**
 * 震动工具类
 * 需要权限: <uses-permission android:name="android.permission.VIBRATE" /> !!
 */
object VibrateUtils {

    private const val TAG = "VibrateUtils"

    /**
     * 震动设备
     * @param context Context 对象
     * @param duration 震动时长（毫秒）
     */
    fun vibrate(context: Context, duration: Long) {
        if (duration <= 0) {
            Log.w(TAG, "Vibration duration must be positive.")
            return
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                val effect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(effect)
                Log.d(TAG, "Vibrating for $duration ms (API S+)")
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
                Log.d(TAG, "Vibrating for $duration ms (deprecated)")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error vibrating device", e)
        }
    }

    /**
     * 按照指定模式震动设备
     * @param context Context 对象
     * @param pattern 震动模式，例如 [0, 100, 1000, 200] 表示等待 0ms，震动 100ms，等待 1000ms，震动 200ms
     * @param repeat 重复次数，-1 表示不重复，0 表示一直重复
     */
    fun vibrate(context: Context, pattern: LongArray, repeat: Int) {
        if (pattern.isEmpty()) {
            Log.w(TAG, "Vibration pattern cannot be empty.")
            return
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                     (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                }
                val effect = VibrationEffect.createWaveform(pattern, repeat)
                vibrator.vibrate(effect)
                Log.d(TAG, "Vibrating with pattern: ${pattern.joinToString()}, repeat: $repeat (API O+)")
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, repeat)
                Log.d(TAG, "Vibrating with pattern: ${pattern.joinToString()}, repeat: $repeat (deprecated)")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error vibrating device with pattern", e)
        }
    }

    /**
     * 取消震动
     * @param context Context 对象
     */
    fun cancel(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                vibrator.cancel()
                Log.d(TAG, "Vibration cancelled (API S+)")
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                @Suppress("DEPRECATION")
                vibrator.cancel()
                Log.d(TAG, "Vibration cancelled (deprecated)")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cancelling vibration", e)
        }
    }
}
