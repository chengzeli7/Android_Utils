package com.miaoyin.weiqi.other.utlis

import android.util.Log
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Fragment 工具类
 * 简化 Fragment 的常见操作
 */
object FragmentUtils {

    private const val TAG = "FragmentUtils"

    /**
     * 添加 Fragment
     * @param fragmentManager FragmentManager 实例
     * @param containerViewId 容器 View 的 ID
     * @param fragment 要添加的 Fragment
     * @param tag Fragment 的 Tag (可选)
     * @param addToBackStack 是否添加到返回栈，默认为 false
     * @param enterAnim 进入动画资源 ID (可选)
     * @param exitAnim 退出动画资源 ID (可选)
     * @param popEnterAnim 返回栈弹出时进入动画资源 ID (可选)
     * @param popExitAnim 返回栈弹出时退出动画资源 ID (可选)
     */
    fun addFragment(
        fragmentManager: FragmentManager,
        @IdRes containerViewId: Int,
        fragment: Fragment,
        tag: String? = null,
        addToBackStack: Boolean = false,
        @AnimRes enterAnim: Int = 0,
        @AnimRes exitAnim: Int = 0,
        @AnimRes popEnterAnim: Int = 0,
        @AnimRes popExitAnim: Int = 0
    ) {
        fragmentManager.beginTransaction().apply {
            // 设置自定义动画
            if (enterAnim != 0 || exitAnim != 0 || popEnterAnim != 0 || popExitAnim != 0) {
                setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
            }
            add(containerViewId, fragment, tag)
            if (addToBackStack) {
                addToBackStack(tag)
            }
            commit()
        }
        Log.d(TAG, "Added Fragment: ${fragment.javaClass.simpleName} to container: $containerViewId")
    }

    /**
     * 替换 Fragment
     * @param fragmentManager FragmentManager 实例
     * @param containerViewId 容器 View 的 ID
     * @param fragment 要替换的 Fragment
     * @param tag Fragment 的 Tag (可选)
     * @param addToBackStack 是否添加到返回栈，默认为 false
     * @param enterAnim 进入动画资源 ID (可选)
     * @param exitAnim 退出动画资源 ID (可选)
     * @param popEnterAnim 返回栈弹出时进入动画资源 ID (可选)
     * @param popExitAnim 返回栈弹出时退出动画资源 ID (可选)
     */
    fun replaceFragment(
        fragmentManager: FragmentManager,
        @IdRes containerViewId: Int,
        fragment: Fragment,
        tag: String? = null,
        addToBackStack: Boolean = false,
        @AnimRes enterAnim: Int = 0,
        @AnimRes exitAnim: Int = 0,
        @AnimRes popEnterAnim: Int = 0,
        @AnimRes popExitAnim: Int = 0
    ) {
        fragmentManager.beginTransaction().apply {
            // 设置自定义动画
            if (enterAnim != 0 || exitAnim != 0 || popEnterAnim != 0 || popExitAnim != 0) {
                setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
            }
            replace(containerViewId, fragment, tag)
            if (addToBackStack) {
                addToBackStack(tag)
            }
            commit()
        }
        Log.d(TAG, "Replaced Fragment with: ${fragment.javaClass.simpleName} in container: $containerViewId")
    }

    /**
     * 移除 Fragment
     * @param fragmentManager FragmentManager 实例
     * @param fragment 要移除的 Fragment
     */
    fun removeFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        fragmentManager.beginTransaction().apply {
            remove(fragment)
            commit()
        }
        Log.d(TAG, "Removed Fragment: ${fragment.javaClass.simpleName}")
    }

    /**
     * 查找指定 Tag 的 Fragment
     * @param fragmentManager FragmentManager 实例
     * @param tag 要查找的 Fragment 的 Tag
     * @return 找到的 Fragment，如果不存在则返回 null
     */
    fun findFragmentByTag(fragmentManager: FragmentManager, tag: String): Fragment? {
        return fragmentManager.findFragmentByTag(tag)
    }

    /**
     * 查找指定容器 ID 的 Fragment
     * @param fragmentManager FragmentManager 实例
     * @param containerViewId 容器 View 的 ID
     * @return 找到的 Fragment，如果不存在则返回 null
     */
    fun findFragmentById(fragmentManager: FragmentManager, @IdRes containerViewId: Int): Fragment? {
        return fragmentManager.findFragmentById(containerViewId)
    }

    /**
     * 显示 Fragment
     * @param fragmentManager FragmentManager 实例
     * @param fragment 要显示的 Fragment
     */
    fun showFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        fragmentManager.beginTransaction().apply {
            show(fragment)
            commit()
        }
        Log.d(TAG, "Showed Fragment: ${fragment.javaClass.simpleName}")
    }

    /**
     * 隐藏 Fragment
     * @param fragmentManager FragmentManager 实例
     * @param fragment 要隐藏的 Fragment
     */
    fun hideFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        fragmentManager.beginTransaction().apply {
            hide(fragment)
            commit()
        }
        Log.d(TAG, "Hid Fragment: ${fragment.javaClass.simpleName}")
    }

    /**
     * 弹出返回栈
     * @param fragmentManager FragmentManager 实例
     */
    fun popBackStack(fragmentManager: FragmentManager) {
        fragmentManager.popBackStack()
        Log.d(TAG, "Popped back stack.")
    }

    /**
     * 弹出返回栈到指定 Tag
     * @param fragmentManager FragmentManager 实例
     * @param tag 要弹出到的 Tag
     * @param inclusive 是否包含指定 Tag 的 Fragment
     */
    fun popBackStack(fragmentManager: FragmentManager, tag: String?, inclusive: Int) {
        fragmentManager.popBackStack(tag, inclusive)
        Log.d(TAG, "Popped back stack to tag: $tag, inclusive: $inclusive")
    }

    // Fragment 状态保存和恢复说明:
    // Fragment 的状态保存和恢复是由 FragmentManager 自动处理的。
    // 当 Activity 或 Fragment 被销毁并重建时 (例如屏幕旋转或系统内存不足)，
    // FragmentManager 会自动保存 Fragment 的状态并在重建后恢复。
    // 开发者通常只需要在 Fragment 中重写 onSaveInstanceState 方法来保存自定义状态，
    // 并在 onCreate 或 onCreateView 中恢复状态即可。
    // 这个工具类主要简化 Fragment 的事务操作，无需在此处额外处理状态保存和恢复的逻辑。

}
