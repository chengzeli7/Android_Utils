package com.miaoyin.weiqi.other.utlis

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import java.io.File
import java.util.UUID
import java.util.regex.Pattern

/**
 * 设备工具类
 */
object DeviceUtils {

    private const val TAG = "DeviceUtils"

    /**
     * 获取设备型号
     * @return 设备型号字符串
     */
    fun getDeviceModel(): String {
        return Build.MODEL ?: "Unknown Model"
    }

    /**
     * 获取设备品牌
     * @return 设备品牌字符串
     */
    fun getDeviceBrand(): String {
        return Build.BRAND ?: "Unknown Brand"
    }

    /**
     * 获取设备制造商
     * @return 设备制造商字符串
     */
    fun getDeviceManufacturer(): String {
        return Build.MANUFACTURER ?: "Unknown Manufacturer"
    }

    /**
     * 获取 Android 系统版本号
     * @return 系统版本号字符串
     */
    fun getSystemVersion(): String {
        return Build.VERSION.RELEASE ?: "Unknown Version"
    }

    /**
     * 获取 Android SDK 版本号
     * @return SDK 版本号
     */
    fun getSystemApiLevel(): Int {
        return Build.VERSION.SDK_INT
    }

    /**
     * 获取设备的唯一标识符 Android ID
     * 注意：Android ID 在设备恢复出厂设置后会改变，并且在某些情况下可能为空或相同
     * @param context Context 对象
     * @return Android ID 字符串
     */
    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "Unknown Android ID"
    }

    /**
     * 获取设备的 IMEI (需要 READ_PHONE_STATE 权限，且在 Android 10+ 获取方式有变化)
     * 推荐使用 Android ID 或其他更现代的标识符
     * @param context Context 对象
     * @return IMEI 字符串，如果无法获取则返回 "Unknown IMEI"
     */
    @SuppressLint("HardwareIds", "MissingPermission")
    fun getIMEI(context: Context): String {
        return try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // 在 Android 10 及以上版本，非系统应用无法直接获取 IMEI
                Log.w(TAG, "IMEI is restricted on Android 10+ for non-system apps.")
                "Restricted on Android 10+"
            } else {
                telephonyManager.deviceId ?: "Unknown IMEI"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting IMEI", e)
            "Error getting IMEI"
        }
    }

    /**
     * 获取设备的唯一标识符 UUID
     * 每次安装应用生成的 UUID 不同，但同一应用多次获取相同
     * @param context Context 对象
     * @return UUID 字符串
     */
    fun getDeviceUUID(context: Context): String {
        // 可以考虑将生成的 UUID 存储在 SharedPreferences 中，以便同一应用多次获取相同
        // 这里简单生成一个新的 UUID
        return UUID.randomUUID().toString()
    }

    /**
     * 获取屏幕宽度（像素）
     * @param context Context 对象
     * @return 屏幕宽度（像素）
     */
    fun getScreenWidth(context: Context): Int {
        // Reusing ScreenUtils method for consistency
        // Assuming ScreenUtils is available and has getScreenWidth method
        // If not, you would need to implement it here or add ScreenUtils as a dependency
        return ScreenUtils.getScreenWidth(context)
    }

    /**
     * 获取屏幕高度（像素）
     * @param context Context 对象
     * @return 屏幕高度（像素）
     */
    fun getScreenHeight(context: Context): Int {
        // Reusing ScreenUtils method for consistency
        // Assuming ScreenUtils is available and has getScreenHeight method
        // If not, you would need to implement it here or add ScreenUtils as a dependency
        return ScreenUtils.getScreenHeight(context)
    }

    /**
     * 获取 CPU 核心数
     * @return CPU 核心数，获取失败返回 -1
     */
    fun getNumberOfCPUCores(): Int {
        return try {
            // More reliable way is to count files in /sys/devices/system/cpu/
            val file = File("/sys/devices/system/cpu/")
            val cpuFiles = file.listFiles { pathname ->
                Pattern.matches("cpu[0-9]+", pathname.name)
            }
            cpuFiles?.size ?: -1
        } catch (e: Exception) {
            Log.e(TAG, "Error getting CPU cores using file system.", e)
            -1
        }
    }

    /**
     * 获取设备总内存大小（字节）
     * @param context Context 对象
     * @return 设备总内存大小（字节），获取失败返回 -1
     */
    fun getTotalMemory(context: Context): Long {
        return try {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            memoryInfo.totalMem
        } catch (e: Exception) {
            Log.e(TAG, "Error getting total memory", e)
            -1L
        }
    }

    /**
     * 获取设备可用内存大小（字节）
     * @param context Context 对象
     * @return 设备可用内存大小（字节），获取失败返回 -1
     */
    fun getAvailableMemory(context: Context): Long {
        return try {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            memoryInfo.availMem
        } catch (e: Exception) {
            Log.e(TAG, "Error getting available memory", e)
            -1L
        }
    }
}
