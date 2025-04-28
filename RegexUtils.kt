package com.miaoyin.weiqi.other.utlis

import android.util.Log
import java.util.regex.Pattern

/**
 * 正则表达式工具类
 */
object RegexUtils {

    private const val TAG = "RegexUtils"

    // 常用的正则表达式模式
    private val REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$" // 手机号简单匹配
    private val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$" // 邮箱
    private val REGEX_URL = "[a-zA-z]+://[^\\s]*" // URL
    private val REGEX_IP_ADDRESS = "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b" // IPv4 地址

    /**
     * 检查字符串是否匹配指定的正则表达式
     * @param input 要检查的字符串
     * @param regex 正则表达式
     * @return 是否匹配
     */
    fun isMatch(input: CharSequence?, regex: String): Boolean {
        if (input == null) {
            Log.w(TAG, "Input string is null.")
            return false
        }
        return Pattern.matches(regex, input)
    }

    /**
     * 检查是否是简单的手机号码格式
     * @param input 要检查的字符串
     * @return 是否是手机号码
     */
    fun isMobileSimple(input: CharSequence?): Boolean {
        return isMatch(input, REGEX_MOBILE_SIMPLE)
    }

    /**
     * 检查是否是邮箱格式
     * @param input 要检查的字符串
     * @return 是否是邮箱
     */
    fun isEmail(input: CharSequence?): Boolean {
        return isMatch(input, REGEX_EMAIL)
    }

    /**
     * 检查是否是 URL 格式
     * @param input 要检查的字符串
     * @return 是否是 URL
     */
    fun isUrl(input: CharSequence?): Boolean {
        return isMatch(input, REGEX_URL)
    }

    /**
     * 检查是否是 IPv4 地址格式
     * @param input 要检查的字符串
     * @return 是否是 IPv4 地址
     */
    fun isIPAddress(input: CharSequence?): Boolean {
        return isMatch(input, REGEX_IP_ADDRESS)
    }

    /**
     * 获取匹配指定正则表达式的所有子字符串
     * @param input 要检查的字符串
     * @param regex 正则表达式
     * @return 匹配的子字符串列表
     */
    fun getMatches(input: CharSequence?, regex: String): List<String> {
        val matches = mutableListOf<String>()
        if (input == null) {
            Log.w(TAG, "Input string is null.")
            return matches
        }
        try {
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(input)
            while (matcher.find()) {
                matches.add(matcher.group())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting regex matches", e)
        }
        return matches
    }

    // TODO: 可以添加更多常用的正则表达式，例如身份证号、邮政编码等
    // TODO: 可以添加替换匹配项的功能
}
