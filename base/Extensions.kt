package com.miaoyin.weiqi.other.base

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment

object Extensions {
    /**
     * Activity扩展 - 安全获取Intent参数
     */
    inline fun <reified T> Activity.getIntentExtra(key: String, default: T): T {
        return intent?.extras?.let {
            when (T::class) {
                String::class -> it.getString(key, default as String) as T
                Int::class -> it.getInt(key, default as Int) as T
                Boolean::class -> it.getBoolean(key, default as Boolean) as T
                Float::class -> it.getFloat(key, default as Float) as T
                Long::class -> it.getLong(key, default as Long) as T
                Double::class -> it.getDouble(key, default as Double) as T
                else -> default
            }
        } ?: default
    }

    /**
     * Fragment扩展 - 安全获取参数
     */
    inline fun <reified T> Fragment.getArgument(key: String, default: T): T {
        return arguments?.let {
            when (T::class) {
                String::class -> it.getString(key, default as String) as T
                Int::class -> it.getInt(key, default as Int) as T
                Boolean::class -> it.getBoolean(key, default as Boolean) as T
                Float::class -> it.getFloat(key, default as Float) as T
                Long::class -> it.getLong(key, default as Long) as T
                Double::class -> it.getDouble(key, default as Double) as T
                else -> default
            }
        } ?: default
    }

    /**
     * View扩展 - 点击防抖
     */
    fun View.setOnSingleClickListener(interval: Long = 500, onClick: (View) -> Unit) {
        var lastClickTime = 0L
        setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > interval) {
                lastClickTime = currentTime
                onClick(it)
            }
        }
    }

    /**
     * EditText扩展 - 文本变化监听
     */
    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    /**
     * String扩展 - 判断字符串是否为空或空白
     */
    fun String?.isNullOrBlank(): Boolean {
        return this == null || this.trim().isEmpty()
    }

    /**
     * Context扩展 - dp转px
     */
    fun Context.dp2px(dpValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * Context扩展 - px转dp
     */
    fun Context.px2dp(pxValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * Context扩展 - sp转px
     */
    fun Context.sp2px(spValue: Float): Int {
        val scale = resources.displayMetrics.scaledDensity
        return (spValue / scale + 0.5f).toInt()
    }
}