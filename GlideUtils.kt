package com.miaoyin.weiqi.other

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity // 如果使用 Fragment，可能需要导入这个
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.io.File

/**
 * Glide 图片加载工具类
 */
object GlideUtils {

    /**
     * 检查 Context 是否有效 (Activity 是否在前台或未销毁)
     *
     * @param context Context 上下文
     * @return Boolean 如果 Context 有效则返回 true，否则返回 false
     */
    private fun isValidContext(context: Context): Boolean {
        // 如果是 Activity 或其子类 (如 FragmentActivity)
        if (context is Activity) {
            // 检查 Activity 是否正在结束或已经被销毁
            if (context.isFinishing || context.isDestroyed) {
                return false
            }
        }
        // 对于 Application Context 或其他非 Activity Context，我们认为它是有效的
        return true
    }

    /**
     * 加载图片（通用方法）
     * 在加载前会检查 Context 状态。
     *
     * @param context Context 上下文
     * @param model Any? 要加载的数据源 (可以是 String url, Uri, File, ResourceId, Bitmap 等)
     * @param imageView ImageView 目标 ImageView
     * @param placeholderResId Int? 占位图资源ID (加载中显示)
     * @param errorResId Int? 错误图资源ID (加载失败显示)
     */
    fun loadImage(
        context: Context,
        model: Any?,
        imageView: ImageView,
        placeholderResId: Int? = null,
        errorResId: Int? = null
    ) {
        // 检查 Context 是否有效
        if (!isValidContext(context)) {
            // Context 无效，不执行加载
            return
        }

        // 构建 Glide 请求
        val requestBuilder = Glide.with(context).load(model)

        // 创建 RequestOptions
        val options = RequestOptions().apply {
            // 设置占位图
            placeholderResId?.let {
                placeholder(it)
            }
            // 设置错误图
            errorResId?.let {
                error(it)
            }
        }

        // 应用 RequestOptions 并加载图片
        requestBuilder.apply(options).into(imageView)
    }

    /**
     * 加载图片（使用 URL）
     * 在加载前会检查 Context 状态。
     *
     * @param context Context 上下文
     * @param url String? 图片 URL
     * @param imageView ImageView 目标 ImageView
     * @param placeholderResId Int? 占位图资源ID (加载中显示)
     * @param errorResId Int? 错误图资源ID (加载失败显示)
     */
    fun loadImage(
        context: Context,
        url: String?,
        imageView: ImageView,
        placeholderResId: Int? = null,
        errorResId: Int? = null
    ) {
        // 检查 Context 是否有效
        if (!isValidContext(context)) {
            // Context 无效，不执行加载
            return
        }
        loadImage(context, url as Any?, imageView, placeholderResId, errorResId)
    }

    /**
     * 加载图片（使用资源 ID）
     * 在加载前会检查 Context 状态。
     *
     * @param context Context 上下文
     * @param resourceId Int 图片资源ID
     * @param imageView ImageView 目标 ImageView
     * @param placeholderResId Int? 占位图资源ID (加载中显示)
     * @param errorResId Int? 错误图资源ID (加载失败显示)
     */
    fun loadImage(
        context: Context,
        resourceId: Int,
        imageView: ImageView,
        placeholderResId: Int? = null,
        errorResId: Int? = null
    ) {
        // 检查 Context 是否有效
        if (!isValidContext(context)) {
            // Context 无效，不执行加载
            return
        }
        loadImage(context, resourceId as Any?, imageView, placeholderResId, errorResId)
    }

    /**
     * 加载图片（使用 File）
     * 在加载前会检查 Context 状态。
     *
     * @param context Context 上下文
     * @param file File? 图片文件
     * @param imageView ImageView 目标 ImageView
     * @param placeholderResId Int? 占位图资源ID (加载中显示)
     * @param errorResId Int? 错误图资源ID (加载失败显示)
     */
    fun loadImage(
        context: Context,
        file: File?,
        imageView: ImageView,
        placeholderResId: Int? = null,
        errorResId: Int? = null
    ) {
        // 检查 Context 是否有效
        if (!isValidContext(context)) {
            // Context 无效，不执行加载
            return
        }
        loadImage(context, file as Any?, imageView, placeholderResId, errorResId)
    }

    /**
     * 加载圆形图片
     * 在加载前会检查 Context 状态。
     *
     * @param context Context 上下文
     * @param model Any? 要加载的数据源
     * @param imageView ImageView 目标 ImageView
     * @param placeholderResId Int? 占位图资源ID
     * @param errorResId Int? 错误图资源ID
     */
    fun loadCircleImage(
        context: Context,
        model: Any?,
        imageView: ImageView,
        placeholderResId: Int? = null,
        errorResId: Int? = null
    ) {
        // 检查 Context 是否有效
        if (!isValidContext(context)) {
            // Context 无效，不执行加载
            return
        }
        // 构建 RequestOptions 并应用圆形裁剪
        val options = RequestOptions.circleCropTransform().apply {
            placeholderResId?.let { placeholder(it) }
            errorResId?.let { error(it) }
        }

        // 加载图片
        Glide.with(context)
            .load(model)
            .apply(options)
            .into(imageView)
    }

    /**
     * 加载圆角图片
     * 在加载前会检查 Context 状态。
     *
     * @param context Context 上下文
     * @param model Any? 要加载的数据源
     * @param imageView ImageView 目标 ImageView
     * @param radius Int 圆角半径 (单位: 像素 px)
     * @param placeholderResId Int? 占位图资源ID
     * @param errorResId Int? 错误图资源ID
     */
    fun loadRoundedImage(
        context: Context,
        model: Any?,
        imageView: ImageView,
        radius: Int,
        placeholderResId: Int? = null,
        errorResId: Int? = null
    ) {
        // 检查 Context 是否有效
        if (!isValidContext(context)) {
            // Context 无效，不执行加载
            return
        }
        // 构建 RequestOptions 并应用圆角转换
        val options = RequestOptions().transform(RoundedCorners(radius)).apply {
            placeholderResId?.let { placeholder(it) }
            errorResId?.let { error(it) }
        }

        // 加载图片
        Glide.with(context)
            .load(model)
            .apply(options)
            .into(imageView)
    }

    /**
     * 加载图片并进行 CenterCrop 裁剪
     * 图片会按比例缩放，使图片的较短边与 ImageView 的对应边相等，然后裁剪掉较长边溢出的部分。
     * 在加载前会检查 Context 状态。
     *
     * @param context Context 上下文
     * @param model Any? 要加载的数据源
     * @param imageView ImageView 目标 ImageView
     * @param placeholderResId Int? 占位图资源ID
     * @param errorResId Int? 错误图资源ID
     */
    fun loadCenterCropImage(
        context: Context,
        model: Any?,
        imageView: ImageView,
        placeholderResId: Int? = null,
        errorResId: Int? = null
    ) {
        // 检查 Context 是否有效
        if (!isValidContext(context)) {
            // Context 无效，不执行加载
            return
        }
        // 构建 RequestOptions 并应用 CenterCrop 裁剪
        val options = RequestOptions.centerCropTransform().apply {
            placeholderResId?.let { placeholder(it) }
            errorResId?.let { error(it) }
        }

        // 加载图片
        Glide.with(context)
            .load(model)
            .apply(options)
            .into(imageView)
    }

    /**
     * 清除指定 ImageView 的 Glide 任务
     * 用于回收资源，避免内存泄漏，特别是在 RecyclerView 或 ViewPager 中使用时。
     *
     * @param context Context 上下文
     * @param imageView ImageView 需要清除任务的 ImageView
     */
    fun clear(context: Context, imageView: ImageView) {
        // 清除任务通常不需要检查 Context 状态，因为它是取消正在进行的任务
        Glide.with(context).clear(imageView)
    }

    /**
     * 清除内存缓存 (需要在主线程调用)
     * 通常不需要手动调用，Glide 会自动管理。但在特定场景下可能需要强制清除。
     *
     * @param context Context 上下文
     */
    fun clearMemoryCache(context: Context) {
        // 清除内存缓存通常不需要检查 Context 状态
        Glide.get(context).clearMemory()
    }

    /**
     * 清除磁盘缓存 (需要在后台线程调用，因为它可能是一个耗时操作)
     *
     * @param context Context 上下文
     */
    fun clearDiskCache(context: Context) {
        // 清除磁盘缓存通常不需要检查 Context 状态
        Thread {
            Glide.get(context).clearDiskCache()
        }.start()
    }
}

