package com.miaoyin.weiqi.other.utlis

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager

/**
 * 屏幕工具类
 */
object ScreenUtils {

    /**
     * 获取屏幕宽度（像素）
     * @param context Context 对象
     * @return 屏幕宽度（像素）
     */
    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度（像素）
     * @param context Context 对象
     * @return 屏幕高度（像素）
     */
    fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    /**
     * 获取屏幕密度
     * @param context Context 对象
     * @return 屏幕密度
     */
    fun getScreenDensity(context: Context): Float {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.density
    }

    /**
     * 获取屏幕密度 DPI
     * @param context Context 对象
     * @return 屏幕密度 DPI
     */
    fun getScreenDensityDpi(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.densityDpi
    }

    /**
     * dp 转 px
     * @param dpValue dp 值
     * @return px 值
     */
    fun dp2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px 转 dp
     * @param pxValue px 值
     * @return dp 值
     */
    fun px2dp(pxValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * sp 转 px
     * @param spValue sp 值
     * @return px 值
     */
    fun sp2px(spValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue,
            Resources.getSystem().displayMetrics
        ).toInt()
    }

    /**
     * px 转 sp
     * @param pxValue px 值
     * @return sp 值
     */
    fun px2sp(pxValue: Float): Int {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }
}
