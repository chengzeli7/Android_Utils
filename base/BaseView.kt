package com.miaoyin.weiqi.other.base

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding

/**
 * 所有自定义 View 的基类，支持 ViewBinding
 *
 * @param VB ViewBinding 类型
 */
abstract class BaseView<VB : ViewBinding> : FrameLayout { // 或者继承 LinearLayout, RelativeLayout 等

    // ViewBinding 实例
    private lateinit var _binding: VB

    // 提供一个非空的 binding 属性，方便在 View 中访问子视图
    protected val binding: VB get() = _binding

    // 日志 Tag，使用当前类的简单名称
    protected val TAG = this.javaClass.simpleName

    // --- 构造函数 ---
    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }
    // --- 构造函数结束 ---


    /**
     * 初始化方法，在所有构造函数中调用
     */
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        Log.d(TAG, "init")
        // 初始化 ViewBinding
        _binding = getViewBinding(LayoutInflater.from(context))

        // 将 ViewBinding 的根视图添加到当前自定义 View 中
        addView(binding.root)

        // 初始化视图和数据
        initView(context, attrs, defStyleAttr, defStyleRes)
        initData()
        initListener()
    }


    /**
     * 获取 ViewBinding 实例
     * 子类必须实现此方法来提供具体的 ViewBinding
     * 例如: CustomViewBinding.inflate(inflater)
     * @param inflater LayoutInflater 实例
     * @return ViewBinding 实例
     */
    abstract fun getViewBinding(inflater: LayoutInflater): VB

    /**
     * 初始化视图
     * 在 init 方法中调用，用于查找 View、设置监听器、处理自定义属性等
     * @param context Context 对象
     * @param attrs AttributeSet 属性集
     * @param defStyleAttr 默认样式属性
     * @param defStyleRes 默认样式资源
     */
    abstract fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)

    /**
     * 初始化数据
     * 在 initView 之后调用，用于加载数据、设置初始状态等
     */
    abstract fun initData()

    abstract fun initListener()

    // TODO: 可以根据需要添加其他生命周期方法，例如 onAttachedToWindow, onDetachedFromWindow 等
    // TODO: 考虑添加处理自定义属性的方法
}
