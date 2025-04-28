package com.miaoyin.weiqi.other.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialog
import androidx.viewbinding.ViewBinding

// 假设存在一个用于简单弹窗的布局文件：layout_base_simple_dialog.xml
// 对应的 ViewBinding 类是 LayoutBaseSimpleDialogBinding
// 请确保你的项目中创建了这个布局文件和对应的 ViewBinding

/**
 * 所有基于 AppCompatDialog 的 Dialog 的基类，支持 ViewBinding
 * 直接继承自 AppCompatDialog
 *
 * @param VB ViewBinding 类型
 */
abstract class BaseDialog<VB : ViewBinding>(
    context: Context,
    @StyleRes themeResId: Int = 0
) : AppCompatDialog(context, themeResId) {

    // ViewBinding 实例
    private lateinit var _binding: VB

    // 提供一个非空的 binding 属性，方便在 Dialog 中访问子视图
    protected val binding: VB get() = _binding

    // 日志 Tag，使用当前类的简单名称
    protected val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        // 初始化 ViewBinding
        _binding = getViewBinding(LayoutInflater.from(context))

        // 设置 Dialog 的内容视图为 ViewBinding 的根视图
        setContentView(binding.root)

        // 初始化视图和数据
        initView(savedInstanceState)
        initData()

        // 可以设置 Dialog 的窗口属性，例如背景、大小等
        // window?.setBackgroundDrawableResource(android.R.color.transparent)
        // window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * 获取 ViewBinding 实例
     * 子类必须实现此方法来提供具体的 ViewBinding
     * 例如: CustomDialogBinding.inflate(inflater)
     * @param inflater LayoutInflater 实例
     * @return ViewBinding 实例
     */
    abstract fun getViewBinding(inflater: LayoutInflater): VB

    /**
     * 初始化视图
     * 在 onCreate 方法中调用，用于查找 View、设置监听器等
     * @param savedInstanceState 保存的状态 Bundle
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     * 在 initView 之后调用，用于加载数据、设置初始状态等
     */
    abstract fun initData()

    /**
     * 设置 Dialog 的宽度
     * @param width 宽度值 (像素或 ViewGroup.LayoutParams 中的常量)
     */
    fun setWidth(width: Int) {
        window?.setLayout(width, window?.attributes?.height ?: ViewGroup.LayoutParams.WRAP_CONTENT)
        Log.d(TAG, "Set dialog width: $width")
    }

    /**
     * 设置 Dialog 的高度
     * @param height 高度值 (像素或 ViewGroup.LayoutParams 中的常量)
     */
    fun setHeight(height: Int) {
        window?.setLayout(window?.attributes?.width ?: ViewGroup.LayoutParams.WRAP_CONTENT, height)
        Log.d(TAG, "Set dialog height: $height")
    }

    /**
     * 设置 Dialog 的布局参数 (宽度和高度)
     * @param width 宽度值 (像素或 ViewGroup.LayoutParams 中的常量)
     * @param height 高度值 (像素或 ViewGroup.LayoutParams 中的常量)
     */
    fun setLayout(width: Int, height: Int) {
        window?.setLayout(width, height)
        Log.d(TAG, "Set dialog layout: width=$width, height=$height")
    }

    /**
     * 设置 Dialog 的重力方向
     * @param gravity 重力方向 (例如 Gravity.CENTER, Gravity.BOTTOM)
     */
    fun setGravity(gravity: Int) {
        window?.setGravity(gravity)
        Log.d(TAG, "Set dialog gravity: $gravity")
    }

    /**
     * 设置 Dialog 的背景
     * @param drawable 背景 Drawable
     */
    fun setBackgroundDrawable(drawable: Drawable?) {
        window?.setBackgroundDrawable(drawable)
        Log.d(TAG, "Set dialog background drawable.")
    }

    /**
     * 设置 Dialog 的窗口动画
     * @param resId 动画资源 ID
     */
    fun setWindowAnimations(@StyleRes resId: Int) {
        window?.setWindowAnimations(resId)
        Log.d(TAG, "Set dialog window animations: $resId")
    }

    /**
     * 设置点击 Dialog 外部是否可以取消 Dialog
     * @param cancel 是否可以取消
     */
    override fun setCanceledOnTouchOutside(cancel: Boolean) {
        super.setCanceledOnTouchOutside(cancel)
        Log.d(TAG, "Set canceled on touch outside: $cancel")
    }

    // --- 静态工厂方法，用于直接在 Activity 中调用显示简单弹窗 ---
    companion object {
        /**
         * 显示一个通用的弹窗，允许外部传入 ViewBinding 和配置视图的 lambda
         * @param context Context 对象 (通常是 Activity)
         * @param viewBindingInflater 用于创建 ViewBinding 的 lambda (例如 MyDialogBinding::inflate)
         * @param cancelable 是否可取消，默认为 true
         * @param dialogConfigListener 用于配置弹窗视图和行为的 lambda，参数为 Dialog 实例和 ViewBinding 实例
         * @return 创建并显示的 Dialog 实例
         */
        fun <VB : ViewBinding> showDialog(
            context: Context,
            viewBindingInflater: (LayoutInflater) -> VB, // 接受一个用于创建 ViewBinding 的 lambda
            cancelable: Boolean = true,
            dialogConfigListener: (dialog: BaseDialog<VB>, binding: VB) -> Unit // 接受一个配置弹窗的 lambda
        ): BaseDialog<VB> {
            val dialog = object : BaseDialog<VB>(context) {
                override fun getViewBinding(inflater: LayoutInflater): VB {
                    // 使用外部传入的 lambda 创建 ViewBinding
                    return viewBindingInflater.invoke(inflater)
                }

                override fun initView(savedInstanceState: Bundle?) {
                    // 调用外部传入的配置 lambda 来设置视图和监听器
                    dialogConfigListener.invoke(this, binding)

                    // 设置是否可取消
                    setCanceledOnTouchOutside(cancelable)
                    setCancelable(cancelable) // 设置返回键是否可取消
                }

                override fun initData() {
                    // 数据初始化可以在外部 lambda 中处理，或者根据需要在此处处理
                }
            }
            dialog.show() // 显示弹窗
            return dialog // 返回 Dialog 实例，以便外部进行更多操作（例如 dismiss）
        }

        // TODO: 可以添加更多静态工厂方法，用于显示其他类型的简单弹窗
        // TODO: 考虑为静态工厂方法添加设置窗口属性的参数 (例如宽度、重力等)
    }
    // --- 静态工厂方法结束 ---
}

// 假设你的项目中存在以下 ViewBinding 类，对应 layout_base_simple_dialog.xml
// import com.your_project_name.databinding.LayoutBaseSimpleDialogBinding

