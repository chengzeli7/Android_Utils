package com.miaoyin.weiqi.other.utlis

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * SharedPreferences 工具类
 * 使用前需要调用 init 方法进行初始化
 */
object SPUtils {

    private const val TAG = "SPUtils"
    private const val DEFAULT_SP_NAME = "app_sp" // 默认的 SharedPreferences 文件名
    private var sp: SharedPreferences? = null

    /**
     * 初始化 SPUtils
     * @param context Context 对象
     * @param spName SharedPreferences 文件名，默认为 "app_sp"
     */
    fun init(context: Context, spName: String = DEFAULT_SP_NAME) {
        sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        Log.d(TAG, "SPUtils initialized with name: $spName")
    }

    private fun getSP(): SharedPreferences {
        return sp ?: throw IllegalStateException("SPUtils not initialized. Call init() first.")
    }

    /**
     * 存储 String 类型数据
     * @param key 键
     * @param value 值
     */
    fun putString(key: String, value: String?) {
        getSP().edit().putString(key, value).apply()
    }

    /**
     * 获取 String 类型数据
     * @param key 键
     * @param defaultValue 默认值
     * @return 对应的值，如果不存在则返回默认值
     */
    fun getString(key: String, defaultValue: String? = null): String? {
        return getSP().getString(key, defaultValue)
    }

    /**
     * 存储 Int 类型数据
     * @param key 键
     * @param value 值
     */
    fun putInt(key: String, value: Int) {
        getSP().edit().putInt(key, value).apply()
    }

    /**
     * 获取 Int 类型数据
     * @param key 键
     * @param defaultValue 默认值
     * @return 对应的值，如果不存在则返回默认值
     */
    fun getInt(key: String, defaultValue: Int = -1): Int {
        return getSP().getInt(key, defaultValue)
    }

    /**
     * 存储 Boolean 类型数据
     * @param key 键
     * @param value 值
     */
    fun putBoolean(key: String, value: Boolean) {
        getSP().edit().putBoolean(key, value).apply()
    }

    /**
     * 获取 Boolean 类型数据
     * @param key 键
     * @param defaultValue 默认值
     * @return 对应的值，如果不存在则返回默认值
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return getSP().getBoolean(key, defaultValue)
    }

    /**
     * 存储 Float 类型数据
     * @param key 键
     * @param value 值
     */
    fun putFloat(key: String, value: Float) {
        getSP().edit().putFloat(key, value).apply()
    }

    /**
     * 获取 Float 类型数据
     * @param key 键
     * @param defaultValue 默认值
     * @return 对应的值，如果不存在则返回默认值
     */
    fun getFloat(key: String, defaultValue: Float = -1f): Float {
        return getSP().getFloat(key, defaultValue)
    }

    /**
     * 存储 Long 类型数据
     * @param key 键
     * @param value 值
     */
    fun putLong(key: String, value: Long) {
        getSP().edit().putLong(key, value).apply()
    }

    /**
     * 获取 Long 类型数据
     * @param key 键
     * @param defaultValue 默认值
     * @return 对应的值，如果不存在则返回默认值
     */
    fun getLong(key: String, defaultValue: Long = -1L): Long {
        return getSP().getLong(key, defaultValue)
    }

    /**
     * 存储 Set<String> 类型数据
     * @param key 键
     * @param value 值
     */
    fun putStringSet(key: String, value: Set<String>?) {
        getSP().edit().putStringSet(key, value).apply()
    }

    /**
     * 获取 Set<String> 类型数据
     * @param key 键
     * @param defaultValue 默认值
     * @return 对应的值，如果不存在则返回默认值
     */
    fun getStringSet(key: String, defaultValue: Set<String>? = null): Set<String>? {
        return getSP().getStringSet(key, defaultValue)
    }

    /**
     * 移除指定键的数据
     * @param key 键
     */
    fun remove(key: String) {
        getSP().edit().remove(key).apply()
    }

    /**
     * 清除所有数据
     */
    fun clear() {
        getSP().edit().clear().apply()
    }

    /**
     * 检查是否包含指定键
     * @param key 键
     * @return 是否包含指定键
     */
    fun contains(key: String): Boolean {
        return getSP().contains(key)
    }

    /**
     * 获取所有键值对
     * @return 所有键值对的 Map
     */
    fun getAll(): Map<String, *> {
        return getSP().all
    }
}
