package com.miaoyin.weiqi.other.utlis

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.math.max

/**
 * 日志工具类
 * 可以在发布版本中控制日志输出和分级过滤，并支持日志写入文件和异步写入
 */
object LogUtils {

    private const val TAG = "AppLog" // 默认的日志 Tag
    private var isDebug = true // 是否是调试模式，控制日志输出

    // 定义日志级别
    object LogLevel {
        const val VERBOSE = 2
        const val DEBUG = 3
        const val INFO = 4
        const val WARN = 5
        const val ERROR = 6
        const val ASSERT = 7
        const val NOTHING = 8 // 不输出任何日志
    }

    private var minLogLevel = LogLevel.VERBOSE // 最小输出日志级别，默认为 Verbose

    // 文件日志相关
    private var isFileLoggingEnabled = false // 是否启用文件日志
    private var logFilePath: String? = null // 日志文件路径
    private val fileLogDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()) // 文件日志时间格式
    private val fileWriterLock = Any() // 用于文件写入的同步锁

    // 日志文件轮转配置
    private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024 // 单个日志文件最大大小 (5MB)
    private const val MAX_OLD_LOG_FILES = 5 // 最多保留的旧日志文件数量

    // 异步写入相关
    private val logMessageQueue = LinkedBlockingQueue<String>() // 日志消息队列
    private var logWriterThread: HandlerThread? = null
    private var logWriterHandler: Handler? = null

    // 初始化异步写入线程
    init {
        startLogWriterThread()
    }

    /**
     * 启动日志写入线程
     */
    private fun startLogWriterThread() {
        if (logWriterThread == null || !logWriterThread!!.isAlive) {
            logWriterThread = HandlerThread("LogWriterThread").apply {
                start()
                logWriterHandler = Handler(looper)
            }
            Log.d(TAG, "Log writer thread started.")
        }
    }

    /**
     * 停止日志写入线程 (可在应用退出时调用，可选)
     */
    fun stopLogWriterThread() {
        logWriterThread?.quitSafely()
        logWriterThread = null
        logWriterHandler = null
        Log.d(TAG, "Log writer thread stopped.")
    }

    /**
     * 设置是否为调试模式
     * 在调试模式下，日志输出受 minLogLevel 控制；非调试模式下，只输出 Error 级别日志。
     * @param debug 是否为调试模式
     */
    fun setDebug(debug: Boolean) {
        isDebug = debug
    }

    /**
     * 设置最小输出日志级别
     * 只有当日志级别大于等于 minLogLevel 且处于调试模式时才会输出。
     * @param level 最小日志级别 (使用 LogLevel 中的常量)
     */
    fun setMinLogLevel(level: Int) {
        minLogLevel = level
    }

    /**
     * 启用文件日志
     * @param filePath 日志文件保存路径 (包括文件名，不带扩展名，例如 /sdcard/logs/applog)
     * 日志文件将以 filePath.log, filePath.1.log, filePath.2.log 等形式保存
     */
    fun enableFileLogging(filePath: String) {
        logFilePath = filePath
        isFileLoggingEnabled = true
        Log.d(TAG, "File logging enabled to: $filePath")
        // 确保日志文件目录存在
        val logFile = File("$filePath.log")
        if (!logFile.parentFile.exists()) {
            logFile.parentFile.mkdirs() // 创建父目录
        }
        // 清理旧的日志文件
        cleanupOldLogFiles()
    }

    /**
     * 禁用文件日志
     */
    fun disableFileLogging() {
        isFileLoggingEnabled = false
        logFilePath = null
        Log.d(TAG, "File logging disabled.")
    }

    /**
     * 输出 Verbose 级别日志
     * @param msg 日志消息
     * @param tag 日志 Tag，默认为 AppLog
     */
    fun v(msg: String?, tag: String = TAG) {
        if (isDebug && minLogLevel <= LogLevel.VERBOSE && msg != null) {
            val logMessage = formatLogMessage(LogLevel.VERBOSE, tag, msg, null)
            Log.v(tag, msg)
            queueLogForWriting(logMessage)
        }
    }

    /**
     * 输出 Debug 级别日志
     * @param msg 日志消息
     * @param tag 日志 Tag，默认为 AppLog
     */
    fun d(msg: String?, tag: String = TAG) {
        if (isDebug && minLogLevel <= LogLevel.DEBUG && msg != null) {
            val logMessage = formatLogMessage(LogLevel.DEBUG, tag, msg, null)
            Log.d(tag, msg)
            queueLogForWriting(logMessage)
        }
    }

    /**
     * 输出 Info 级别日志
     * @param msg 日志消息
     * @param tag 日志 Tag，默认为 AppLog
     */
    fun i(msg: String?, tag: String = TAG) {
        if (isDebug && minLogLevel <= LogLevel.INFO && msg != null) {
            val logMessage = formatLogMessage(LogLevel.INFO, tag, msg, null)
            Log.i(tag, msg)
            queueLogForWriting(logMessage)
        }
    }

    /**
     * 输出 Warn 级别日志
     * @param msg 日志消息
     * @param tag 日志 Tag，默认为 AppLog
     */
    fun w(msg: String?, tag: String = TAG) {
        if (isDebug && minLogLevel <= LogLevel.WARN && msg != null) {
            val logMessage = formatLogMessage(LogLevel.WARN, tag, msg, null)
            Log.w(tag, msg)
            queueLogForWriting(logMessage)
        }
    }

    /**
     * 输出 Error 级别日志
     * @param msg 日志消息
     * @param tag 日志 Tag，默认为 AppLog
     * @param tr 异常对象
     */
    fun e(msg: String?, tag: String = TAG, tr: Throwable? = null) {
        // Error 级别日志即使在非调试模式下也可能需要输出，这里根据 isDebug 和 minLogLevel 控制
        if ((isDebug && minLogLevel <= LogLevel.ERROR) || !isDebug) {
            val logMessage = formatLogMessage(LogLevel.ERROR, tag, msg, tr)
            if (msg != null) {
                Log.e(tag, msg, tr)
            } else if (tr != null) {
                Log.e(tag, "Error", tr)
            }
            queueLogForWriting(logMessage)
        }
    }

    /**
     * 格式化日志消息
     * @param level 日志级别
     * @param tag 日志 Tag
     * @param msg 日志消息
     * @param tr 异常对象
     * @return 格式化后的日志字符串
     */
    private fun formatLogMessage(level: Int, tag: String, msg: String?, tr: Throwable?): String {
        val timestamp = fileLogDateFormat.format(Date())
        val threadName = Thread.currentThread().name
        val levelChar = when (level) {
            LogLevel.VERBOSE -> "V"
            LogLevel.DEBUG -> "D"
            LogLevel.INFO -> "I"
            LogLevel.WARN -> "W"
            LogLevel.ERROR -> "E"
            LogLevel.ASSERT -> "A"
            else -> "-"
        }

        // 获取调用栈信息
        val stackTrace = Thread.currentThread().stackTrace
        var callerInfo = ""
        // 遍历堆栈，找到实际调用 LogUtils 方法的位置
        for (element in stackTrace) {
            if (element.className != LogUtils::class.java.name &&
                element.className.indexOf("java.lang.Thread") != 0 && // 排除Thread内部调用
                element.className.indexOf("com.lib.utils.LogUtils") != 0 // 排除LogUtils内部调用
            ) {
                callerInfo = "(${element.fileName}:${element.lineNumber})"
                break
            }
        }

        val message = msg ?: ""
        val exceptionStackTrace = tr?.stackTraceToString() ?: ""

        // 格式: Timestamp [ThreadName] Level/Tag (CallerInfo): Message ExceptionStackTrace
        return "$timestamp [$threadName] $levelChar/$tag $callerInfo: $message $exceptionStackTrace".trim()
    }


    /**
     * 将日志消息加入队列等待写入文件
     * @param logMessage 要写入的日志消息
     */
    private fun queueLogForWriting(logMessage: String) {
        if (!isFileLoggingEnabled || logFilePath == null) {
            return
        }
        // 将消息加入队列，异步写入线程会处理
        logMessageQueue.offer(logMessage)
        // 确保异步写入任务被调度
        logWriterHandler?.post(writeLogRunnable)
    }

    /**
     * 异步写入日志文件的 Runnable
     */
    private val writeLogRunnable = Runnable {
        synchronized(fileWriterLock) {
            try {
                val currentLogFile = File("$logFilePath.log")

                // 检查文件大小，如果超过限制则进行轮转
                if (currentLogFile.exists() && currentLogFile.length() >= MAX_FILE_SIZE_BYTES) {
                    rotateLogFiles()
                }

                // 从队列中取出所有待写入的日志消息
                val messagesToWrite = mutableListOf<String>()
                logMessageQueue.drainTo(messagesToWrite)

                if (messagesToWrite.isNotEmpty()) {
                    FileWriter(currentLogFile, true).use { writer ->
                        for (message in messagesToWrite) {
                            writer.append("$message\n")
                        }
                        writer.flush()
                    }
                }
            } catch (e: IOException) {
                // 这里不能使用 LogUtils.e，否则可能导致死循环或阻塞
                Log.e(TAG, "Error writing log to file: $logFilePath.log", e)
            }
        }
    }


    /**
     * 轮转日志文件
     * 将当前的 log.log 文件重命名为 log.1.log，log.1.log 重命名为 log.2.log，以此类推
     * 并删除超过最大数量的旧日志文件
     */
    private fun rotateLogFiles() {
        if (logFilePath == null) return

        // 删除最旧的日志文件
        val oldestFile = File("$logFilePath.${MAX_OLD_LOG_FILES}.log")
        if (oldestFile.exists()) {
            oldestFile.delete()
            Log.d(TAG, "Deleted oldest log file: ${oldestFile.absolutePath}")
        }

        // 重命名现有日志文件
        for (i in MAX_OLD_LOG_FILES - 1 downTo 0) {
            val oldFile = if (i == 0) File("$logFilePath.log") else File("$logFilePath.$i.log")
            val newFile = File("$logFilePath.${i + 1}.log")
            if (oldFile.exists()) {
                oldFile.renameTo(newFile)
                Log.d(TAG, "Rotated log file: ${oldFile.absolutePath} to ${newFile.absolutePath}")
            }
        }
        Log.d(TAG, "Log file rotation complete.")
    }

    /**
     * 清理超过最大数量的旧日志文件
     */
    private fun cleanupOldLogFiles() {
        if (logFilePath == null) return

        val logDir = File(logFilePath!!).parentFile
        if (!logDir.exists() || !logDir.isDirectory) return

        val logFiles = logDir.listFiles { _, name ->
            name.startsWith(File(logFilePath!!).name) && name.endsWith(".log")
        }

        if (logFiles == null || logFiles.size <= MAX_OLD_LOG_FILES) return

        // 按修改时间排序文件，删除最旧的
        logFiles.sortBy { it.lastModified() }

        for (i in 0 until logFiles.size - MAX_OLD_LOG_FILES) {
            logFiles[i].delete()
            Log.d(TAG, "Cleaned up old log file: ${logFiles[i].absolutePath}")
        }
        Log.d(TAG, "Old log file cleanup complete.")
    }

    // TODO: 考虑更优雅的线程关闭方式，例如在 Application 的 onTerminate 中调用 stopLogWriterThread
    // TODO: 可以添加文件日志的开关状态持久化
}
