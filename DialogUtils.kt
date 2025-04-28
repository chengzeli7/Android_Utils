package com.miaoyin.weiqi.other.utlis

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log

/**
 * 对话框工具类
 * 简化各种对话框的创建和显示
 */
object DialogUtils {

    private const val TAG = "DialogUtils"

    /**
     * 显示基本的 AlertDialog
     * @param context Context 对象
     * @param title 对话框标题
     * @param message 对话框消息
     * @param positiveButtonText 确定按钮文本
     * @param positiveButtonListener 确定按钮点击监听器
     * @param negativeButtonText 取消按钮文本 (可选)
     * @param negativeButtonListener 取消按钮点击监听器 (可选)
     * @param cancelButtonText 中立按钮文本 (可选)
     * @param cancelButtonListener 中立按钮点击监听器 (可选)
     * @param cancelable 是否可取消，默认为 true
     * @return 创建的 AlertDialog 实例
     */
    fun showAlertDialog(
        context: Context,
        title: CharSequence?,
        message: CharSequence?,
        positiveButtonText: CharSequence,
        positiveButtonListener: DialogInterface.OnClickListener? = null,
        negativeButtonText: CharSequence? = null,
        negativeButtonListener: DialogInterface.OnClickListener? = null,
        cancelButtonText: CharSequence? = null,
        cancelButtonListener: DialogInterface.OnClickListener? = null,
        cancelable: Boolean = true
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText, positiveButtonListener)
            .setCancelable(cancelable)

        negativeButtonText?.let {
            builder.setNegativeButton(it, negativeButtonListener)
        }
        cancelButtonText?.let {
            builder.setNeutralButton(it, cancelButtonListener)
        }

        val dialog = builder.create()
        dialog.show()
        Log.d(TAG, "Showing AlertDialog with title: $title")
        return dialog
    }

    /**
     * 显示带有列表项的 AlertDialog
     * @param context Context 对象
     * @param title 对话框标题
     * @param items 列表项数组
     * @param onItemClickListener 列表项点击监听器
     * @param cancelable 是否可取消，默认为 true
     * @return 创建的 AlertDialog 实例
     */
    fun showListAlertDialog(
        context: Context,
        title: CharSequence?,
        items: Array<CharSequence>,
        onItemClickListener: DialogInterface.OnClickListener,
        cancelable: Boolean = true
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setItems(items, onItemClickListener)
            .setCancelable(cancelable)

        val dialog = builder.create()
        dialog.show()
        Log.d(TAG, "Showing ListAlertDialog with title: $title")
        return dialog
    }

    /**
     * 显示带有单选列表的 AlertDialog
     * @param context Context 对象
     * @param title 对话框标题
     * @param items 列表项数组
     * @param checkedItem 默认选中的项的索引
     * @param onItemClickListener 单选列表项点击监听器
     * @param positiveButtonText 确定按钮文本
     * @param positiveButtonListener 确定按钮点击监听器
     * @param cancelable 是否可取消，默认为 true
     * @return 创建的 AlertDialog 实例
     */
    fun showSingleChoiceAlertDialog(
        context: Context,
        title: CharSequence?,
        items: Array<CharSequence>,
        checkedItem: Int,
        onItemClickListener: DialogInterface.OnClickListener,
        positiveButtonText: CharSequence,
        positiveButtonListener: DialogInterface.OnClickListener? = null,
        cancelable: Boolean = true
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setSingleChoiceItems(items, checkedItem, onItemClickListener)
            .setPositiveButton(positiveButtonText, positiveButtonListener)
            .setCancelable(cancelable)

        val dialog = builder.create()
        dialog.show()
        Log.d(TAG, "Showing SingleChoiceAlertDialog with title: $title")
        return dialog
    }

    /**
     * 显示带有复选列表的 AlertDialog
     * @param context Context 对象
     * @param title 对话框标题
     * @param items 列表项数组
     * @param checkedItems 默认选中的项的布尔数组
     * @param onItemClickListener 复选列表项点击监听器
     * @param positiveButtonText 确定按钮文本
     * @param positiveButtonListener 确定按钮点击监听器
     * @param cancelable 是否可取消，默认为 true
     * @return 创建的 AlertDialog 实例
     */
    fun showMultiChoiceAlertDialog(
        context: Context,
        title: CharSequence?,
        items: Array<CharSequence>,
        checkedItems: BooleanArray,
        onItemClickListener: DialogInterface.OnMultiChoiceClickListener,
        positiveButtonText: CharSequence,
        positiveButtonListener: DialogInterface.OnClickListener? = null,
        cancelable: Boolean = true
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMultiChoiceItems(items, checkedItems, onItemClickListener)
            .setPositiveButton(positiveButtonText, positiveButtonListener)
            .setCancelable(cancelable)

        val dialog = builder.create()
        dialog.show()
        Log.d(TAG, "Showing MultiChoiceAlertDialog with title: $title")
        return dialog
    }


    /**
     * 显示 ProgressDialog (已废弃，推荐使用 ProgressBar 或 Material Design 的 ProgressIndicator)
     * @param context Context 对象
     * @param message 进度消息
     * @param cancelable 是否可取消，默认为 false
     * @return 创建的 ProgressDialog 实例
     */
    @Suppress("DEPRECATION")
    fun showProgressDialog(
        context: Context,
        message: CharSequence?,
        cancelable: Boolean = false
    ): ProgressDialog {
        val dialog = ProgressDialog(context).apply {
            setMessage(message)
            setCancelable(cancelable)
            setProgressStyle(ProgressDialog.STYLE_SPINNER) // 或者 ProgressDialog.STYLE_HORIZONTAL
        }
        dialog.show()
        Log.d(TAG, "Showing ProgressDialog with message: $message")
        return dialog
    }

    // TODO: 添加自定义布局的 Dialog
    // TODO: 考虑使用 DialogFragment 来管理对话框的生命周期
    // TODO: 考虑使用 Material Components 的 Dialogs
}
