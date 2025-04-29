package com.miaoyin.weiqi.other
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * 所有 RecyclerView Adapter 的基类，支持 ViewBinding
 * 支持多种 Item 类型、DiffUtil、加载更多和空视图
 *
 * @param T 数据类型
 */
abstract class BaseRecyclerViewAdapter<T>(
    private var dataList: MutableList<T> = mutableListOf() // 数据列表
) : RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<T, *>>() { // ViewHolder 类型使用 BaseViewHolder 基类，泛型通配符，指定数据类型 T

    // 日志 Tag，使用当前类的简单名称
    protected val TAG = this.javaClass.simpleName

    // Item 点击监听器
    private var onItemClickListener: ((item: T, position: Int) -> Unit)? = null

    // Item 长按监听器
    private var onItemLongClickListener: ((item: T, position: Int) -> Boolean)? = null

    // 加载更多相关
    private var onLoadMoreListener: (() -> Unit)? = null
    private var isLoadingMore = false // 是否正在加载更多
    private var loadMoreThreshold = 5 // 触发加载更多的阈值，距离列表末尾的 Item 数量

    // 空视图
    private var emptyView: View? = null
    private var recyclerView: RecyclerView? = null // 需要 RecyclerView 实例来控制空视图的显示/隐藏

    /**
     * 设置 Item 点击监听器
     * @param listener 点击监听器的 lambda，参数为数据项和位置
     */
    fun setOnItemClickListener(listener: (item: T, position: Int) -> Unit) {
        onItemClickListener = listener
    }

    /**
     * 设置 Item 长按监听器
     * @param listener 长按监听器的 lambda，参数为数据项和位置，返回 Boolean 表示是否消费事件
     */
    fun setOnItemLongClickListener(listener: (item: T, position: Int) -> Boolean) {
        onItemLongClickListener = listener
    }

    /**
     * 设置加载更多监听器和阈值
     * @param threshold 触发加载更多的阈值，距离列表末尾的 Item 数量，默认为 5
     * @param listener 加载更多监听器的 lambda
     */
    fun setOnLoadMoreListener(threshold: Int = 5, listener: () -> Unit) {
        loadMoreThreshold = threshold
        onLoadMoreListener = listener
        Log.d(TAG, "Load more listener set with threshold: $threshold")
    }

    /**
     * 设置加载更多状态
     * @param loading 是否正在加载更多
     */
    fun setLoadingMore(loading: Boolean) {
        isLoadingMore = loading
        Log.d(TAG, "Set loading more: $loading")
    }

    /**
     * 设置空视图
     * @param emptyView 空视图 View
     */
    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
        checkEmptyViewVisibility() // 检查并更新空视图可见性
        Log.d(TAG, "Empty view set.")
    }

    /**
     * ViewHolder 基类，持有 ViewBinding 实例
     * 子类需要继承此基类，并指定具体的 ViewBinding 类型
     *
     * @param T 数据类型
     * @param VB ViewBinding 类型
     * @param binding ViewBinding 实例
     */
    abstract class BaseViewHolder<T, VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        init {
            // Item 点击和长按事件的监听器设置将在 Adapter 中处理
        }

        /**
         * 绑定数据到 ViewHolder
         * 子类需要实现此方法来处理具体的数据绑定逻辑
         *
         * @param item 当前位置的数据项
         */
        abstract fun bindData(item: T) // bindData now takes the specific item type T
    }

    /**
     * 创建 ViewHolder
     * 子类必须实现此方法，根据 viewType 创建并返回对应的 BaseViewHolder 子类实例
     * 返回类型是 BaseViewHolder<T, *>, indicating it holds the specific data type T
     */
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T, *>

    /**
     * 绑定数据到 ViewHolder
     */
    override fun onBindViewHolder(holder: BaseViewHolder<T, *>, position: Int) { // Holder type is BaseViewHolder<T, *>
        Log.d(TAG, "onBindViewHolder, position: $position")
        // 获取当前位置的数据
        val item = dataList[position]
        // 调用 ViewHolder 的 bindData method
        holder.bindData(item) // Pass the item directly

        // Set Item click listener
        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClickListener?.invoke(dataList[adapterPosition], adapterPosition)
                Log.d(TAG, "Item clicked at position: $adapterPosition")
            }
        }

        // Set Item long click listener
        holder.itemView.setOnLongClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemLongClickListener?.invoke(dataList[adapterPosition], adapterPosition) ?: false
            } else {
                false
            }
        }

        // Check if load more is needed
        if (onLoadMoreListener != null && !isLoadingMore && position >= itemCount - 1 - loadMoreThreshold) {
            isLoadingMore = true // 设置正在加载更多标志
            onLoadMoreListener?.invoke() // 触发加载更多回调
            Log.d(TAG, "Triggering load more at position: $position")
        }
    }

    /**
     * 获取 Item 数量
     */
    override fun getItemCount(): Int {
        val count = dataList.size
        checkEmptyViewVisibility() // 在获取数量时检查空视图可见性
        return count
    }

    /**
     * 获取指定位置的数据项
     * @param position 位置
     * @return 数据项
     */
    fun getItem(position: Int): T {
        return dataList[position]
    }

    /**
     * 获取所有数据项
     * @return 数据列表
     */
    fun getDataList(): MutableList<T> {
        return dataList
    }

    /**
     * 更新数据列表并刷新 Adapter (使用 DiffUtil)
     * 推荐使用此方法进行数据更新，可以提高效率并带有动画效果
     * @param newList 新的数据列表
     * @param diffCallback DiffUtil.Callback 实例
     */
    fun submitList(newList: List<T>, diffCallback: DiffUtil.Callback) {
        val oldList = dataList.toList() // 创建一个副本用于 DiffUtil 比较
        val result = DiffUtil.calculateDiff(diffCallback)

        dataList.clear()
        dataList.addAll(newList)

        result.dispatchUpdatesTo(this)
        checkEmptyViewVisibility() // 检查并更新空视图可见性
        Log.d(TAG, "Data list updated using DiffUtil, new size: ${dataList.size}")
    }

    /**
     * 更新数据列表并刷新 Adapter (简单粗暴刷新)
     * 不推荐用于大量数据更新，效率较低
     * @param newData 新的数据列表
     */
    fun submitList(newData: List<T>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
        checkEmptyViewVisibility() // 检查并更新空视图可见性
        Log.d(TAG, "Data list updated with notifyDataSetChanged, new size: ${dataList.size}")
    }


    /**
     * 添加数据到列表末尾并刷新 Adapter
     * @param newData 要添加的新数据列表
     */
    fun addData(newData: List<T>) {
        val startPosition = dataList.size
        dataList.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
        checkEmptyViewVisibility() // 检查并更新空视图可见性
        Log.d(TAG, "Added ${newData.size} items, total size: ${dataList.size}")
    }

    /**
     * 添加单个数据项到列表末尾并刷新 Adapter
     * @param data 要添加的数据项
     */
    fun addData(data: T) {
        val position = dataList.size
        dataList.add(data)
        notifyItemInserted(position)
        checkEmptyViewVisibility() // 检查并更新空视图可见性
        Log.d(TAG, "Added 1 item, total size: ${dataList.size}")
    }

    /**
     * 移除指定位置的数据项并刷新 Adapter
     * @param position 要移除的数据项的位置
     */
    fun removeData(position: Int) {
        if (position >= 0 && position < dataList.size) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
            checkEmptyViewVisibility() // 检查并更新空视图可见性
            Log.d(TAG, "Removed item at position: $position, total size: ${dataList.size}")
        } else {
            Log.w(TAG, "Invalid position for removal: $position")
        }
    }

    /**
     * 移除指定数据项并刷新 Adapter
     * @param data 要移除的数据项
     */
    fun removeData(data: T) {
        val position = dataList.indexOf(data)
        if (position != -1) {
            removeData(position)
        } else {
            Log.w(TAG, "Data item not found for removal.")
        }
    }

    /**
     * 清空数据列表并刷新 Adapter
     */
    fun clearData() {
        dataList.clear()
        notifyDataSetChanged()
        checkEmptyViewVisibility() // 检查并更新空视图可见性
        Log.d(TAG, "Data list cleared.")
    }

    /**
     * 检查并更新空视图的可见性
     */
    private fun checkEmptyViewVisibility() {
        if (emptyView == null || recyclerView == null) {
            return
        }
        if (dataList.isEmpty()) {
            emptyView?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
            Log.d(TAG, "Empty view is visible, RecyclerView is gone.")
        } else {
            emptyView?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
            Log.d(TAG, "Empty view is gone, RecyclerView is visible.")
        }
    }

    // 在 Adapter 绑定到 RecyclerView 时获取 RecyclerView 实例
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        checkEmptyViewVisibility() // 绑定时检查空视图可见性
        Log.d(TAG, "Adapter attached to RecyclerView.")
    }

    // 在 Adapter 从 RecyclerView 解绑时清空 RecyclerView 实例
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
        Log.d(TAG, "Adapter detached from RecyclerView.")
    }

    // TODO: 考虑添加 Item 拖拽和滑动删除功能
}



