package com.miaoyin.weiqi.other.utlis

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import java.util.Stack

/**
 * Activity 工具类
 */
object ActivityUtils {

    private const val TAG = "ActivityUtils"
    // 使用 Stack 来管理 Activity 栈 (注意并发问题，实际项目中可能需要更复杂的管理)
    private val activityStack = Stack<Activity>()

    /**
     * 将 Activity 加入栈中
     * @param activity 要加入的 Activity
     */
    fun pushActivity(activity: Activity) {
        activityStack.push(activity)
        Log.d(TAG, "Pushed Activity: ${activity.javaClass.simpleName}, Stack size: ${activityStack.size}")
    }

    /**
     * 将 Activity 从栈中移除
     * @param activity 要移除的 Activity
     */
    fun popActivity(activity: Activity) {
        activityStack.remove(activity)
        Log.d(TAG, "Popped Activity: ${activity.javaClass.simpleName}, Stack size: ${activityStack.size}")
    }

    /**
     * 获取当前栈顶的 Activity
     * @return 栈顶的 Activity，如果栈为空则返回 null
     */
    fun currentActivity(): Activity? {
        return if (activityStack.isNotEmpty()) {
            activityStack.peek()
        } else {
            null
        }
    }

    /**
     * 结束当前栈顶的 Activity
     */
    fun finishCurrentActivity() {
        currentActivity()?.finish()
    }

    /**
     * 结束指定类的 Activity
     * @param activityClass 要结束的 Activity 类
     */
    fun finishActivity(activityClass: Class<*>) {
        activityStack.filter { it.javaClass == activityClass }
            .forEach { it.finish() }
    }

    /**
     * 结束所有 Activity
     */
    fun finishAllActivities() {
        while (activityStack.isNotEmpty()) {
            activityStack.pop().finish()
        }
    }

    /**
     * 启动 Activity
     * @param context Context 对象
     * @param activityClass 要启动的 Activity 类
     * @param bundle 传递的参数 Bundle
     */
    fun startActivity(context: Context, activityClass: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(context, activityClass)
        bundle?.let { intent.putExtras(it) }
        // 如果 context 不是 Activity，需要添加 FLAG_ACTIVITY_NEW_TASK
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
        Log.d(TAG, "Started Activity: ${activityClass.simpleName}")
    }

    /**
     * 检查 Activity 是否存在
     * @param context Context 对象
     * @param packageName 包名
     * @param className Activity 类名
     * @return Activity 是否存在
     */
    fun isActivityExists(context: Context, packageName: String, className: String): Boolean {
        val intent = Intent()
        intent.setClassName(packageName, className)
        return try {
            context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null
        } catch (e: Exception) {
            Log.e(TAG, "Error checking if activity exists", e)
            false
        }
    }

    // TODO: 可以添加更多 Activity 相关的操作，例如获取根 Activity、获取指定 Activity 实例等
}
