package com.miaoyin.weiqi.other.utlis

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.util.Log

/**
 * 键盘工具类
 */
object KeyboardUtils {

    private const val TAG = "KeyboardUtils"

    /**
     * 显示软键盘
     * @param view 依附的 View
     */
    fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        Log.d(TAG, "Showing keyboard for view: ${view.javaClass.simpleName}")
    }

    /**
     * 隐藏软键盘
     * @param view 依附的 View
     */
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        Log.d(TAG, "Hiding keyboard for view: ${view.javaClass.simpleName}")
    }

    /**
     * 切换软键盘显示/隐藏状态
     * @param context Context 对象
     */
    fun toggleKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, 0)
        Log.d(TAG, "Toggling keyboard visibility")
    }

    // TODO: 可以添加更多键盘相关的操作，例如判断键盘是否打开
}
