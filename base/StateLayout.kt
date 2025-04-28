package com.miaoyin.weiqi.other.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

/**
 * StateLayout - 多状态布局管理器
 * 配合BaseActivity和BaseFragment的页面状态管理使用
 */
class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    // 内容视图
    private var contentView: View? = null
    
    // 加载中视图
    private var loadingView: View? = null
    
    // 空数据视图
    private var emptyView: View? = null
    
    // 错误视图
    private var errorView: View? = null
    
    // 网络错误视图
    private var netErrorView: View? = null
    
    // 当前显示的视图
    private var currentView: View? = null
    
    /**
     * 设置内容视图
     */
    fun setContentView(view: View) {
        if (contentView !== view) {
            removeView(contentView)
            contentView = view
            addView(contentView)
            showContent()
        }
    }
    
    /**
     * 设置加载中视图
     */
    fun setLoadingView(view: View) {
        if (loadingView !== view) {
            if (currentView === loadingView) {
                showContent()
            }
            removeView(loadingView)
            loadingView = view
            addView(loadingView)
            loadingView?.visibility = View.GONE
        }
    }
    
    /**
     * 设置空数据视图
     */
    fun setEmptyView(view: View) {
        if (emptyView !== view) {
            if (currentView === emptyView) {
                showContent()
            }
            removeView(emptyView)
            emptyView = view
            addView(emptyView)
            emptyView?.visibility = View.GONE
        }
    }
    
    /**
     * 设置错误视图
     */
    fun setErrorView(view: View) {
        if (errorView !== view) {
            if (currentView === errorView) {
                showContent()
            }
            removeView(errorView)
            errorView = view
            addView(errorView)
            errorView?.visibility = View.GONE
        }
    }
    
    /**
     * 设置网络错误视图
     */
    fun setNetErrorView(view: View) {
        if (netErrorView !== view) {
            if (currentView === netErrorView) {
                showContent()
            }
            removeView(netErrorView)
            netErrorView = view
            addView(netErrorView)
            netErrorView?.visibility = View.GONE
        }
    }
    
    /**
     * 显示内容视图
     */
    fun showContent() {
        switchView(contentView)
    }
    
    /**
     * 显示加载中视图
     */
    fun showLoading() {
        switchView(loadingView)
    }
    
    /**
     * 显示空数据视图
     */
    fun showEmpty() {
        switchView(emptyView)
    }
    
    /**
     * 显示错误视图
     */
    fun showError() {
        switchView(errorView)
    }
    
    /**
     * 显示网络错误视图
     */
    fun showNetError() {
        switchView(netErrorView)
    }
    
    /**
     * 根据状态显示对应视图
     */
    fun showViewByState(state: BaseActivity.PageState) {
        when (state) {
            BaseActivity.PageState.NORMAL -> showContent()
            BaseActivity.PageState.LOADING -> showLoading()
            BaseActivity.PageState.EMPTY -> showEmpty()
            BaseActivity.PageState.ERROR -> showError()
            BaseActivity.PageState.NET_ERROR -> showNetError()
        }
    }
    
    /**
     * 切换视图
     */
    private fun switchView(view: View?) {
        if (view == null) return
        
        if (currentView !== view) {
            contentView?.visibility = View.GONE
            loadingView?.visibility = View.GONE
            emptyView?.visibility = View.GONE
            errorView?.visibility = View.GONE
            netErrorView?.visibility = View.GONE
            
            view.visibility = View.VISIBLE
            currentView = view
        }
    }
    
    /**
     * 设置错误视图重试点击监听器
     */
    fun setOnRetryClickListener(listener: OnClickListener) {
        //todo
//        errorView?.findViewById<View>(R.id.btn_retry)?.setOnClickListener(listener)
//        netErrorView?.findViewById<View>(R.id.btn_retry)?.setOnClickListener(listener)
    }
}