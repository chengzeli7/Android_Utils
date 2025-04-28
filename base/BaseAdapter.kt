package com.miaoyin.weiqi.other.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * RecyclerView适配器基类
 * @param T 数据类型
 * @param VH ViewHolder类型
 */
abstract class BaseAdapter<T, VH : BaseAdapter.BaseViewHolder> :
    RecyclerView.Adapter<VH>() {

    // 数据集合
    protected val dataList = mutableListOf<T>()

    // 点击监听器
    private var onItemClickListener: ((T, Int) -> Unit)? = null
    private var onItemLongClickListener: ((T, Int) -> Boolean)? = null

    /**
     * 获取列表项数量
     */
    override fun getItemCount(): Int = dataList.size

    /**
     * 绑定数据到ViewHolder
     */
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = dataList[position]
        holder.bind(item)

        // 设置点击事件
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item, position)
        }

        // 设置长按事件
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.invoke(item, position) ?: false
        }
    }

    /**
     * 更新数据列表
     */
    fun submitList(list: List<T>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * 添加数据项
     */
    fun addItem(item: T) {
        dataList.add(item)
        notifyItemInserted(dataList.size - 1)
    }

    /**
     * 添加数据列表
     */
    fun addItems(list: List<T>) {
        val startPos = dataList.size
        dataList.addAll(list)
        notifyItemRangeInserted(startPos, list.size)
    }

    /**
     * 移除数据项
     */
    fun removeItem(position: Int) {
        if (position in dataList.indices) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * 设置点击监听器
     */
    fun setOnItemClickListener(listener: (T, Int) -> Unit) {
        this.onItemClickListener = listener
    }

    /**
     * 设置长按监听器
     */
    fun setOnItemLongClickListener(listener: (T, Int) -> Boolean) {
        this.onItemLongClickListener = listener
    }

    /**
     * 基础ViewHolder
     */
    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * 绑定数据到视图
         */
        abstract fun <T> bind(item: T)
    }
    /**
     * 带ViewBinding的ViewHolder基类
     */
    abstract class BindingViewHolder<VB : ViewBinding>(
        protected val binding: VB
    ) : BaseViewHolder(binding.root)
}

