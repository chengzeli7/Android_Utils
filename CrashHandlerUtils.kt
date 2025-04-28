package com.miaoyin.weiqi.other.utlis.new

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.system.exitProcess

/**
 * 崩溃处理工具类
 * 用于捕获应用未捕获的异常并进行处理（例如保存崩溃日志）
 */
class CrashHandlerUtils private constructor() : Thread.UncaughtExceptionHandler {

    private val TAG = "CrashHandlerUtils"
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    private var mContext: Context? = null

    // 用于格式化日期,作为日志文件名的一部分
    private val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())

    // 用于存储设备信息和异常信息
    private val infos: MutableMap<String, String> = mutableMapOf()

    /**
     * 初始化崩溃处理器
     * @param context Context 对象
     */
    fun init(context: Context) {
        mContext = context
        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 设置该 CrashHandlerUtils 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
        Log.d(TAG, "CrashHandlerUtils initialized.")
    }

    /**
     * 当 UncaughtException 发生时会转入该函数进行处理
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler?.uncaughtException(thread, ex)
        } else {
            try {
                Thread.sleep(3000) // 暂停 3 秒，等待日志写入等操作完成
            } catch (e: InterruptedException) {
                Log.e(TAG, "Error during sleep in uncaughtException", e)
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        }
    }

    /**
     * 自定义错误处理，收集设备信息，发送错误报告等操作均在此完成
     * @param ex 崩溃异常
     * @return 是否处理了该异常
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        // 收集设备参数信息
        collectDeviceInfo(mContext!!)
        // 保存日志文件
        saveCrashInfoToFile(ex)
        // TODO: 可以添加发送错误报告到服务器的逻辑
        // TODO: 可以在这里启动一个 Activity 显示崩溃信息或引导用户提交反馈

        // 在主线程中显示 Toast (可选，不推荐在崩溃时做太多 UI 操作)
        // new Thread {
        //     override fun run() {
        //         Looper.prepare()
        //         Toast.makeText(mContext, "程序出现异常，即将退出", Toast.LENGTH_LONG).show()
        //         Looper.loop()
        //     }
        // }.start()

        return true // 表示已处理异常
    }

    /**
     * 收集设备参数信息
     * @param ctx Context 对象
     */
    private fun collectDeviceInfo(ctx: Context) {
        try {
            val pm = ctx.packageManager
            val pi = pm.getPackageInfo(ctx.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = pi.versionName ?: "null"
                val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    pi.longVersionCode.toString()
                } else {
                    @Suppress("DEPRECATION")
                    pi.versionCode.toString()
                }
                infos["versionName"] = versionName
                infos["versionCode"] = versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Error collecting package info", e)
        }
        // 收集 Build 类中的所有信息
        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                infos[field.name] = field.get(null).toString()
            } catch (e: Exception) {
                Log.e(TAG, "Error collecting build info", e)
            }
        }
    }

    /**
     * 保存崩溃信息到文件
     * @param ex 崩溃异常
     * @return 保存文件的路径
     */
    private fun saveCrashInfoToFile(ex: Throwable): String? {
        val sb = StringBuffer()
        for ((key, value) in infos) {
            sb.append("$key=$value\n")
        }

        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)

        try {
            val timestamp = System.currentTimeMillis()
            val time = formatter.format(Date())
            val fileName = "crash-$time-$timestamp.log"

            // 获取应用内部存储的缓存目录
            val crashDir = File(mContext!!.cacheDir, "crash_logs")
            if (!crashDir.exists()) {
                crashDir.mkdirs()
            }
            val crashFile = File(crashDir, fileName)

            FileWriter(crashFile).use { writer ->
                writer.write(sb.toString())
                writer.flush()
            }
            Log.e(TAG, "Crash log saved to: ${crashFile.absolutePath}")
            return crashFile.absolutePath
        } catch (e: Exception) {
            Log.e(TAG, "Error saving crash log", e)
        }
        return null
    }

    companion object {
        @Volatile
        private var INSTANCE: CrashHandlerUtils? = null

        /**
         * 获取 CrashHandlerUtils 实例
         * @return CrashHandlerUtils 实例
         */
        fun getInstance(): CrashHandlerUtils {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CrashHandlerUtils().also { INSTANCE = it }
            }
        }
    }

    // TODO: 添加将崩溃日志上传到服务器的功能
    // TODO: 考虑在崩溃时显示一个友好的错误提示界面
    // TODO: 考虑处理 ANR (Application Not Responding)
}
