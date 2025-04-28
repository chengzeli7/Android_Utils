package com.miaoyin.weiqi.other.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType
import kotlin.coroutines.CoroutineContext

/**
 * 扩展的Fragment基类
 * 在原有基础上增加了懒加载优化、权限管理、事件总线、页面状态管理等功能
 * @param VB ViewBinding类型
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment(), CoroutineScope {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 200
    }

    // 协程相关
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    // ViewBinding
    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    // 是否可见
    private var isVisibleToUser = false

    // 是否已创建视图
    private var isViewCreated = false

    // 是否已加载数据
    private var isDataInitialized = false

    // 页面状态管理
    protected var pageState: BaseActivity.PageState = BaseActivity.PageState.NORMAL
        set(value) {
            field = value
            updatePageState(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化协程作用域
        job = Job()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 初始化ViewBinding
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz = type.actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        _binding = method.invoke(null, inflater, container, false) as VB
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true

        // 初始化视图
        initView()

        // 初始化监听器
        initListeners()

        // 判断是否需要加载数据
        if (isVisibleToUser && !isDataInitialized && lazyLoadEnabled()) {
            initData()
            isDataInitialized = true
        }
    }

    /**
     * 是否启用懒加载
     * 默认启用，子类可覆盖返回false禁用懒加载
     */
    protected open fun lazyLoadEnabled(): Boolean = true

    /**
     * 设置Fragment可见性
     * 用于ViewPager等场景下的Fragment懒加载
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser

        // 判断是否需要加载数据
        if (isVisibleToUser && isViewCreated && !isDataInitialized && lazyLoadEnabled()) {
            initData()
            isDataInitialized = true
        }
    }

    override fun onResume() {
        super.onResume()
        // 处理非ViewPager场景下的懒加载
        if (!isDataInitialized && !lazyLoadEnabled()) {
            initData()
            isDataInitialized = true
        }
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
     * 是否需要注册事件总线，默认不需要
     * 子类如需使用EventBus，覆盖此方法返回true
     */
    protected open fun needEventBus(): Boolean = false

    /**
     * 更新页面状态
     * 子类可以覆盖此方法，根据不同状态显示不同的视图（如加载中、空数据、错误等）
     */
    protected open fun updatePageState(state: BaseActivity.PageState) {
        // 默认实现为空，子类根据需要覆盖
    }

    /**
     * 重新加载数据
     */
    protected fun refreshData() {
        isDataInitialized = false
        initData()
        isDataInitialized = true
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 显示长Toast信息
     */
    protected fun showLongToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    /**
     * 显示Snackbar
     */
    protected fun showSnackbar(
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        action: String? = null,
        actionListener: View.OnClickListener? = null
    ) {
        val snackbar = Snackbar.make(binding.root, message, duration)
        if (action != null && actionListener != null) {
            snackbar.setAction(action, actionListener)
        }
        snackbar.show()
    }

    /**
     * 页面跳转
     */
    protected inline fun <reified T : Activity> startActivity(
        noinline block: (Intent.() -> Unit)? = null
    ) {
        val intent = Intent(requireContext(), T::class.java)
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
        val intent = Intent(requireContext(), T::class.java)
        block?.invoke(intent)
        startActivityForResult(intent, requestCode)
    }

    /**
     * 隐藏软键盘
     */
    protected fun hideSoftKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = requireActivity().currentFocus
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
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 处理EventBus消息，子类需要覆盖此方法
     */
    protected open fun handleEventBusMessage(event: Any) {
        // 默认为空实现
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 释放ViewBinding，避免内存泄漏
        _binding = null
        isViewCreated = false
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消所有协程
        job.cancel()

    }
}