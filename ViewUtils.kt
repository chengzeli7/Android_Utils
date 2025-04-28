package com.miaoyin.weiqi.other.utlis.new

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver

/**
 * View 工具类
 * 提供一些 View 相关的便捷操作
 */
object ViewUtils {

    private const val TAG = "ViewUtils"

    /**
     * 设置 View 的可见性为 Visible
     * @param view 要设置的 View
     */
    fun setVisible(view: View) {
        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
            Log.d(TAG, "Set view visible: ${view.javaClass.simpleName}")
        }
    }

    /**
     * 设置 View 的可见性为 Invisible (保留空间)
     * @param view 要设置的 View
     */
    fun setInvisible(view: View) {
        if (view.visibility != View.INVISIBLE) {
            view.visibility = View.INVISIBLE
            Log.d(TAG, "Set view invisible: ${view.javaClass.simpleName}")
        }
    }

    /**
     * 设置 View 的可见性为 Gone (不保留空间)
     * @param view 要设置的 View
     */
    fun setGone(view: View) {
        if (view.visibility != View.GONE) {
            view.visibility = View.GONE
            Log.d(TAG, "Set view gone: ${view.javaClass.simpleName}")
        }
    }

    /**
     * 判断 View 是否可见
     * @param view 要判断的 View
     * @return View 是否可见
     */
    fun isVisible(view: View): Boolean {
        return view.visibility == View.VISIBLE
    }

    /**
     * 判断 View 是否不可见 (保留空间)
     * @param view 要判断的 View
     * @return View 是否不可见
     */
    fun isInvisible(view: View): Boolean {
        return view.visibility == View.INVISIBLE
    }

    /**
     * 判断 View 是否 Gone (不保留空间)
     * @param view 要判断的 View
     * @return View 是否 Gone
     */
    fun isGone(view: View): Boolean {
        return view.visibility == View.GONE
    }

    /**
     * 获取 View 的宽度（像素）
     * @param view 要获取尺寸的 View
     * @return View 的宽度（像素）
     */
    fun getViewWidth(view: View): Int {
        return view.width
    }

    /**
     * 获取 View 的高度（像素）
     * @param view 要获取尺寸的 View
     * @return View 的高度（像素）
     */
    fun getViewHeight(view: View): Int {
        return view.height
    }

    /**
     * 测量 View 的尺寸
     * 在 View 未绘制完成时获取准确尺寸
     * @param view 要测量的 View
     * @param callback 测量完成后的回调，参数为 View 的宽度和高度
     */
    fun measureView(view: View, callback: (width: Int, height: Int) -> Unit) {
        view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                view.viewTreeObserver.removeOnPreDrawListener(this)
                callback(view.width, view.height)
                Log.d(
                    TAG,
                    "Measured view: ${view.javaClass.simpleName}, width: ${view.width}, height: ${view.height}"
                )
                return true
            }
        })
    }

    /**
     * 获取 View 的截图
     * @param view 要截图的 View
     * @return View 的 Bitmap 截图
     */
    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        Log.d(TAG, "Captured bitmap from view: ${view.javaClass.simpleName}")
        return bitmap
    }

    // TODO: 添加设置 View 的 Margin 和 Padding
    // TODO: 添加查找子 View 的方法
    // TODO: 考虑 View 的事件处理相关方法
}
