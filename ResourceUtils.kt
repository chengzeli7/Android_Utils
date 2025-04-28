package com.miaoyin.weiqi.other.utlis

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.BoolRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

/**
 * 资源工具类
 */
object ResourceUtils {

    private const val TAG = "ResourceUtils"

    /**
     * 获取颜色资源
     * @param context Context 对象
     * @param colorResId 颜色资源的 ID
     * @return 颜色值
     */
    @ColorInt
    fun getColor(context: Context, @ColorRes colorResId: Int): Int {
        return try {
            ContextCompat.getColor(context, colorResId)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting color resource with ID: $colorResId", e)
            // Return a default color or throw an exception as appropriate
            0 // Example: return black color
        }
    }

    /**
     * 获取字符串资源
     * @param context Context 对象
     * @param stringResId 字符串资源的 ID
     * @return 字符串
     */
    fun getString(context: Context, @StringRes stringResId: Int): String {
        return try {
            context.getString(stringResId)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting string resource with ID: $stringResId", e)
            "" // Return empty string or handle error as appropriate
        }
    }

    /**
     * 获取带格式化参数的字符串资源
     * @param context Context 对象
     * @param stringResId 字符串资源的 ID
     * @param formatArgs 格式化参数
     * @return 格式化后的字符串
     */
    fun getString(context: Context, @StringRes stringResId: Int, vararg formatArgs: Any): String {
        return try {
            context.getString(stringResId, *formatArgs)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting formatted string resource with ID: $stringResId", e)
            "" // Return empty string or handle error as appropriate
        }
    }

    /**
     * 获取 Drawable 资源
     * @param context Context 对象
     * @param drawableResId Drawable 资源的 ID
     * @return Drawable 对象
     */
    fun getDrawable(context: Context, @DrawableRes drawableResId: Int): Drawable? {
        return try {
            ContextCompat.getDrawable(context, drawableResId)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting drawable resource with ID: $drawableResId", e)
            null // Return null or handle error as appropriate
        }
    }

    /**
     * 获取尺寸资源 (例如 dimens.xml 中的尺寸)
     * @param context Context 对象
     * @param dimenResId 尺寸资源的 ID
     * @return 尺寸值 (浮点型)
     */
    fun getDimension(context: Context, @DimenRes dimenResId: Int): Float {
        return try {
            context.resources.getDimension(dimenResId)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting dimension resource with ID: $dimenResId", e)
            0f // Return 0 or handle error as appropriate
        }
    }

    /**
     * 获取整数资源
     * @param context Context 对象
     * @param integerResId 整数资源的 ID
     * @return 整数值
     */
    fun getInteger(context: Context, @IntegerRes integerResId: Int): Int {
        return try {
            context.resources.getInteger(integerResId)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting integer resource with ID: $integerResId", e)
            0 // Return 0 or handle error as appropriate
        }
    }

    /**
     * 获取布尔值资源
     * @param context Context 对象
     * @param booleanResId 布尔值资源的 ID
     * @return 布尔值
     */
    fun getBoolean(context: Context, @BoolRes booleanResId: Int): Boolean {
        return try {
            context.resources.getBoolean(booleanResId)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting boolean resource with ID: $booleanResId", e)
            false // Return false or handle error as appropriate
        }
    }

    // TODO: 可以添加更多资源相关的获取方法，例如获取动画、布局等
}
