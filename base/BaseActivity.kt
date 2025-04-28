package com.miaoyin.weiqi.other.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType
import kotlin.coroutines.CoroutineContext

/**
 * Activity基类
 * 集成了ViewBinding、协程作用域和基础生命周期管理、权限管理、状态栏配置、事件总线、页面状态管理等功能
 * @param VB ViewBinding类型
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), CoroutineScope {

    // 协程相关
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    // ViewBinding
    protected lateinit var binding: VB

    // 页面状态管理
    protected var pageState: PageState = PageState.NORMAL
        set(value) {
            field = value
            updatePageState(value)
        }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化协程作用域
        job = Job()

        // 状态栏配置
        configStatusBar()

        // 初始化ViewBinding
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz = type.actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(null, layoutInflater) as VB
        setContentView(binding.root)

        // 初始化视图
        initView()

        // 初始化数据
        initData()

        // 初始化监听器
        initListeners()

        // 订阅LiveEventBus事件
        registerLiveEventBus()
    }

    /**
     * 初始化视图
     */
    protected abstract fun initView()

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 初始化监听器
     */
    protected abstract fun initListeners()

    /**
     * 注册LiveEventBus事件
     * 在此方法中调用observeLiveEvent方法订阅事件
     */
    protected open fun registerLiveEventBus() {
        // 默认为空实现，子类根据需要覆盖此方法
    }

    /**
     * 订阅LiveEventBus事件
     * @param eventName 事件名称
     * @param observer 事件观察者回调
     */
    protected fun <T> observeLiveEvent(eventName: String, observer: (T) -> Unit) {
        val liveDataObserver = Observer<T> { data ->
            observer.invoke(data)
        }

        LiveEventBus.get<T>(eventName)
            .observe(this, liveDataObserver)
    }

    /**
     * 订阅LiveEventBus事件（粘性）
     * @param eventName 事件名称
     * @param observer 事件观察者回调
     */
    protected fun <T> observeStickyLiveEvent(eventName: String, observer: (T) -> Unit) {
        val liveDataObserver = Observer<T> { data ->
            observer.invoke(data)
        }

        LiveEventBus.get<T>(eventName)
            .observeSticky(this, liveDataObserver)
    }

    /**
     * 发送LiveEventBus事件
     * @param eventName 事件名称
     * @param data 事件数据
     */
    protected fun <T> postLiveEvent(eventName: String, data: T) {
        LiveEventBus.get<T>(eventName).post(data)
    }

    /**
     * 延迟发送LiveEventBus事件
     * @param eventName 事件名称
     * @param data 事件数据
     * @param delay 延迟时间（毫秒）
     */
    protected fun <T> postLiveEventDelay(eventName: String, data: T, delay: Long) {
        LiveEventBus.get<T>(eventName).postDelay(data, delay)
    }

    /**
     * 配置状态栏
     * 默认实现为透明状态栏，子类可覆盖此方法自定义状态栏样式
     */
    protected open fun configStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT

            // 设置状态栏字体颜色为深色
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    /**
     * 设置状态栏颜色
     */
    protected fun setStatusBarColor(@ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
        }
    }

    /**
     * 设置导航栏颜色
     */
    protected fun setNavigationBarColor(@ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = color
        }
    }

    /**
     * 全屏显示
     */
    protected fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    /**
     * 更新页面状态
     * 子类可以覆盖此方法，根据不同状态显示不同的视图（如加载中、空数据、错误等）
     */
    protected open fun updatePageState(state: PageState) {
        // 默认实现为空，子类根据需要覆盖
    }

    /**
     * 在UI线程执行协程
     */
    protected fun launchOnUI(block: suspend CoroutineScope.() -> Unit): Job {
        return launch { block() }
    }

    /**
     * 在IO线程执行协程
     */
    protected fun launchOnIO(block: suspend CoroutineScope.() -> Unit): Job {
        return launch(Dispatchers.IO) { block() }
    }

    /**
     * 显示Toast信息
     */
    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 显示长Toast信息
     */
    protected fun showLongToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     * 页面跳转
     */
    protected inline fun <reified T : Activity> startActivity(
        noinline block: (Intent.() -> Unit)? = null
    ) {
        val intent = Intent(this, T::class.java)
        block?.invoke(intent)
        startActivity(intent)
    }

    /**
     * 带请求码的页面跳转
     */
    protected inline fun <reified T : Activity> startActivityForResult(
        requestCode: Int,
        noinline block: (Intent.() -> Unit)? = null
    ) {
        val intent = Intent(this, T::class.java)
        block?.invoke(intent)
        startActivityForResult(intent, requestCode)
    }

    /**
     * 隐藏软键盘
     */
    protected fun hideSoftKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = currentFocus
        if (currentFocusView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
    }

    /**
     * 显示软键盘
     */
    protected fun showSoftKeyboard(view: View) {
        view.requestFocus()
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消所有协程
        job.cancel()

    }

    /**
     * 页面状态枚举
     */
    enum class PageState {
        NORMAL,      // 正常
        LOADING,     // 加载中
        EMPTY,       // 空数据
        ERROR,       // 错误
        NET_ERROR    // 网络错误
    }
}