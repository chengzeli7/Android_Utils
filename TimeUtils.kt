package com.miaoyin.weiqi.other.utlis

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 时间工具类
 */
object TimeUtils {

    private const val TAG = "TimeUtils"
    private const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss" // 默认日期时间格式

    /**
     * 将时间戳转换为指定格式的日期时间字符串
     * @param timeMillis 时间戳（毫秒）
     * @param format 目标日期时间格式，默认为 "yyyy-MM-dd HH:mm:ss"
     * @return 格式化后的日期时间字符串
     */
    fun formatTime(timeMillis: Long, format: String = DEFAULT_DATE_FORMAT): String {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.format(Date(timeMillis))
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Invalid date format: $format", e)
            "" // Return empty string or handle error as appropriate
        }
    }

    /**
     * 将日期时间字符串转换为时间戳
     * @param timeString 日期时间字符串
     * @param format 日期时间字符串的格式，默认为 "yyyy-MM-dd HH:mm:ss"
     * @return 对应的时间戳（毫秒），如果解析失败则返回 -1
     */
    fun parseTime(timeString: String, format: String = DEFAULT_DATE_FORMAT): Long {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.parse(timeString)?.time ?: -1L
        } catch (e: ParseException) {
            Log.e(TAG, "Error parsing time string: $timeString with format $format", e)
            -1L // Return -1 or handle error as appropriate
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Invalid date format: $format", e)
            -1L
        }
    }

    /**
     * 获取当前时间的时间戳（毫秒）
     * @return 当前时间的时间戳
     */
    fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    /**
     * 获取当前日期时间字符串
     * @param format 目标日期时间格式，默认为 "yyyy-MM-dd HH:mm:ss"
     * @return 当前日期时间字符串
     */
    fun getCurrentTimeString(format: String = DEFAULT_DATE_FORMAT): String {
        return formatTime(getCurrentTimeMillis(), format)
    }

    /**
     * 计算两个时间戳之间的差值（毫秒）
     * @param time1 时间戳1
     * @param time2 时间戳2
     * @return 时间差（毫秒），始终为非负值
     */
    fun getTimeDifferenceMillis(time1: Long, time2: Long): Long {
        return Math.abs(time1 - time2)
    }

    /**
     * 计算两个日期时间字符串之间的差值（毫秒）
     * @param timeString1 日期时间字符串1
     * @param timeString2 日期时间字符串2
     * @param format 日期时间字符串的格式，默认为 "yyyy-MM-dd HH:mm:ss"
     * @return 时间差（毫秒），如果解析失败则返回 -1
     */
    fun getTimeDifferenceMillis(timeString1: String, timeString2: String, format: String = DEFAULT_DATE_FORMAT): Long {
        val timeMillis1 = parseTime(timeString1, format)
        val timeMillis2 = parseTime(timeString2, format)
        return if (timeMillis1 != -1L && timeMillis2 != -1L) {
            getTimeDifferenceMillis(timeMillis1, timeMillis2)
        } else {
            Log.e(TAG, "Failed to calculate time difference due to parsing error.")
            -1L
        }
    }

    // TODO: 可以添加更多时间相关的操作，例如获取年、月、日、星期等
}
