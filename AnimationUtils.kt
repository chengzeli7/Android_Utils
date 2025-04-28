package com.miaoyin.weiqi.other.utlis.new

import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation

/**
 * 动画工具类
 * 简化一些常用的动画效果
 */
object AnimationUtils {

    private const val TAG = "AnimationUtils"

    /**
     * 创建一个透明度动画
     * @param fromAlpha 起始透明度 (0.0 - 1.0)
     * @param toAlpha 结束透明度 (0.0 - 1.0)
     * @param duration 动画时长（毫秒）
     * @param fillAfter 动画结束后是否保持结束状态
     * @return 透明度动画
     */
    fun createAlphaAnimation(
        fromAlpha: Float, toAlpha: Float, duration: Long, fillAfter: Boolean = false
    ): AlphaAnimation {
        val animation = AlphaAnimation(fromAlpha, toAlpha).apply {
            this.duration = duration
            this.fillAfter = fillAfter
        }
        Log.d(TAG, "Created AlphaAnimation: from=$fromAlpha, to=$toAlpha, duration=$duration")
        return animation
    }

    /**
     * 创建一个缩放动画
     * @param fromX 起始 X 缩放比例
     * @param toX 结束 X 缩放比例
     * @param fromY 起始 Y 缩放比例
     * @param toY 结束 Y 缩放比例
     * @param pivotX 缩放中心点的 X 坐标类型 (Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_PARENT)
     * @param pivotXValue 缩放中心点的 X 坐标值
     * @param pivotY 缩放中心点的 Y 坐标类型
     * @param pivotYValue 缩放中心点的 Y 坐标值
     * @param duration 动画时长（毫秒）
     * @param fillAfter 动画结束后是否保持结束状态
     * @return 缩放动画
     */
    fun createScaleAnimation(
        fromX: Float,
        toX: Float,
        fromY: Float,
        toY: Float,
        pivotX: Int = Animation.RELATIVE_TO_SELF,
        pivotXValue: Float = 0.5f,
        pivotY: Int = Animation.RELATIVE_TO_SELF,
        pivotYValue: Float = 0.5f,
        duration: Long,
        fillAfter: Boolean = false
    ): ScaleAnimation {
        val animation = ScaleAnimation(
            fromX, toX, fromY, toY, pivotX, pivotXValue, pivotY, pivotYValue
        ).apply {
            this.duration = duration
            this.fillAfter = fillAfter
        }
        Log.d(
            TAG, "Created ScaleAnimation: from=($fromX,$fromY), to=($toX,$toY), duration=$duration"
        )
        return animation
    }

    /**
     * 创建一个旋转动画
     * @param fromDegrees 起始旋转角度
     * @param toDegrees 结束旋转角度
     * @param pivotX 旋转中心点的 X 坐标类型
     * @param pivotXValue 旋转中心点的 X 坐标值
     * @param pivotY 旋转中心点的 Y 坐标类型
     * @param pivotYValue 旋转中心点的 Y 坐标值
     * @param duration 动画时长（毫秒）
     * @param fillAfter 动画结束后是否保持结束状态
     * @return 旋转动画
     */
    fun createRotateAnimation(
        fromDegrees: Float,
        toDegrees: Float,
        pivotX: Int = Animation.RELATIVE_TO_SELF,
        pivotXValue: Float = 0.5f,
        pivotY: Int = Animation.RELATIVE_TO_SELF,
        pivotYValue: Float = 0.5f,
        duration: Long,
        fillAfter: Boolean = false
    ): RotateAnimation {
        val animation = RotateAnimation(
            fromDegrees, toDegrees, pivotX, pivotXValue, pivotY, pivotYValue
        ).apply {
            this.duration = duration
            this.fillAfter = fillAfter
        }
        Log.d(TAG, "Created RotateAnimation: from=$fromDegrees, to=$toDegrees, duration=$duration")
        return animation
    }

    /**
     * 创建一个平移动画
     * @param fromXDelta 起始 X 坐标偏移量
     * @param toXDelta 结束 X 坐标偏移量
     * @param fromYDelta 起始 Y 坐标偏移量
     * @param toYDelta 结束 Y 坐标偏移量
     * @param duration 动画时长（毫秒）
     * @param fillAfter 动画结束后是否保持结束状态
     * @return 平移动画
     */
    fun createTranslateAnimation(
        fromXDelta: Float,
        toXDelta: Float,
        fromYDelta: Float,
        toYDelta: Float,
        duration: Long,
        fillAfter: Boolean = false
    ): TranslateAnimation {
        val animation = TranslateAnimation(
            fromXDelta, toXDelta, fromYDelta, toYDelta
        ).apply {
            this.duration = duration
            this.fillAfter = fillAfter
        }
        Log.d(
            TAG,
            "Created TranslateAnimation: from=($fromXDelta,$fromYDelta), to=($toXDelta,$toYDelta), duration=$duration"
        )
        return animation
    }

    /**
     * 创建一个动画集合
     * @param shareInterpolator 集合中的动画是否共享同一个插值器
     * @param animations 动画数组
     * @return 动画集合
     */
    fun createAnimationSet(
        shareInterpolator: Boolean = true, vararg animations: Animation
    ): AnimationSet {
        val animationSet = AnimationSet(shareInterpolator).apply {
            animations.forEach { addAnimation(it) }
        }
        Log.d(TAG, "Created AnimationSet with ${animations.size} animations.")
        return animationSet
    }

    /**
     * 启动动画
     * @param view 要应用动画的 View
     * @param animation 要应用的动画
     * @param listener 动画监听器 (可选)
     */
    fun startAnimation(
        view: View, animation: Animation, listener: Animation.AnimationListener? = null
    ) {
        animation.setAnimationListener(listener)
        view.startAnimation(animation)
        Log.d(TAG, "Started animation on view: ${view.javaClass.simpleName}")
    }

    // TODO: 添加 ValueAnimator 和 ObjectAnimator 的创建方法 (属性动画)
    // TODO: 添加常用的插值器创建方法
    // TODO: 考虑动画 XML 资源的加载
}
