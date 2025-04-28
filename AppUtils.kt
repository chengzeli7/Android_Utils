package com.miaoyin.weiqi.other.utlis

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log

/**
 * 应用工具类
 */
object AppUtils {

    private const val TAG = "AppUtils"

    /**
     * 获取应用版本名称
     * @param context Context 对象
     * @return 应用版本名称，获取失败返回空字符串
     */
    fun getAppVersionName(context: Context): String {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: ""
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Error getting app version name", e)
            ""
        }
    }

    /**
     * 获取应用版本号
     * @param context Context 对象
     * @return 应用版本号，获取失败返回 -1
     */
    fun getAppVersionCode(context: Context): Long {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Error getting app version code", e)
            -1
        }
    }

    /**
     * 获取应用包名
     * @param context Context 对象
     * @return 应用包名
     */
    fun getAppPackageName(context: Context): String {
        return context.packageName
    }

    /**
     * 检查应用是否安装
     * @param context Context 对象
     * @param packageName 要检查的应用包名
     * @return 应用是否安装
     */
    fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * 启动其他应用
     * @param context Context 对象
     * @param packageName 要启动的应用包名
     * @return 是否成功启动应用
     */
    fun launchApp(context: Context, packageName: String): Boolean {
        return try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                Log.d(TAG, "Launched app: $packageName")
                true
            } else {
                Log.w(TAG, "Launch intent not found for package: $packageName")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error launching app: $packageName", e)
            false
        }
    }

    /**
     * 跳转到应用商店页面
     * @param context Context 对象
     * @param packageName 应用包名，默认为当前应用包名
     */
    fun goToAppMarket(context: Context, packageName: String = context.packageName) {
        try {
            val uri = Uri.parse("market://details?id=$packageName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            Log.d(TAG, "Going to app market for package: $packageName")
        } catch (e: Exception) {
            Log.e(TAG, "Error going to app market for package: $packageName", e)
            // 如果没有安装应用商店，可以尝试跳转到网页版
            try {
                val uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                Log.d(TAG, "Going to web app market for package: $packageName")
            } catch (e2: Exception) {
                Log.e(TAG, "Error going to web app market for package: $packageName", e2)
            }
        }
    }

    // TODO: 可以添加获取应用图标、应用名称、签名信息等方法
}
