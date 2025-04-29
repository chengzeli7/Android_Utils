package com.miaoyin.weiqi.other

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale

/**
 * 图片下载和保存到相册工具类 使用Glide
 */
object DownLoadImageUtil {

    private const val TAG = "DownLoadImageUtil" // 日志标签

    /**
     * 下载网络图片并保存到相册
     * 需要提前申请以下权限:
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> (Android 10 以下)
     * 在 Android 10 (API 29) 及以上版本，推荐使用 Scoped Storage，直接保存到公共目录通常不需要 WRITE_EXTERNAL_STORAGE 权限，
     * 但需要确保应用有读写公共目录的权限（通常系统会处理）。
     *
     * @param context Context 对象，建议使用 Application Context 避免内存泄漏
     * @param url 图片的网络 URL
     * @param onSuccess 保存成功回调，返回保存的文件路径
     * @param onFail 保存失败回调，返回异常信息
     */
    fun downloadAndSaveImageToGallery(
        context: Context,
        url: String?,
        onSuccess: (filePath: String) -> Unit,
        onFail: (e: Exception?) -> Unit
    ) {
        if (url.isNullOrEmpty()) {
            Log.e(TAG, "Image URL is null or empty.")
            onFail(IllegalArgumentException("Image URL is null or empty"))
            return
        }

        // 使用 Glide 的 downloadOnly() 功能直接下载文件到缓存
        Glide.with(context)
            .downloadOnly()
            .load(url)
            .listener(object : RequestListener<File> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<File>,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e(TAG, "Image download failed: ${e?.message}", e)
                    onFail(e) // 下载失败，调用失败回调
                    return false // 不消费事件，允许 Glide 继续处理（尽管这里没有后续处理）
                }

                override fun onResourceReady(
                    resource: File, // Glide 下载好的文件（在缓存中）
                    model: Any,
                    target: Target<File>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d(TAG, "Image downloaded successfully to cache: ${resource.absolutePath}")
                    // 下载成功，将缓存文件复制到相册目录
                    saveFileToGallery(context, resource, url, onSuccess, onFail)
                    return false // 不消费事件
                }
            })
            .submit() // 提交下载任务
    }

    /**
     * 将下载好的文件保存到相册目录
     *
     * @param context Context 对象
     * @param downloadedFile Glide 下载到缓存的临时文件
     * @param originalUrl 原始图片 URL，用于尝试获取文件扩展名
     * @param onSuccess 保存成功回调
     * @param onFail 保存失败回调
     */
    private fun saveFileToGallery(
        context: Context,
        downloadedFile: File,
        originalUrl: String,
        onSuccess: (filePath: String) -> Unit,
        onFail: (e: Exception?) -> Unit
    ) {
        // 获取相册的公共目录
        val galleryDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (galleryDir == null) {
            Log.e(TAG, "Failed to get public pictures directory.")
            onFail(IOException("Failed to get public pictures directory"))
            return
        }

        // 确保相册目录存在
        if (!galleryDir.exists()) {
            galleryDir.mkdirs() // 创建目录（如果不存在）
        }

        // 生成保存的文件名
        val fileName = generateFileName(originalUrl)
        val destFile = File(galleryDir, fileName)

        // 复制文件
        try {
            copyFile(downloadedFile, destFile)
            Log.d(TAG, "File copied to gallery: ${destFile.absolutePath}")

            // 刷新相册，使图片出现在相册中
            scanMediaFile(context, destFile.absolutePath) {
                // 刷新完成后调用成功回调
                onSuccess(destFile.absolutePath)
                Log.d(TAG, "Media scan complete for: ${destFile.absolutePath}")
            }

        } catch (e: IOException) {
            Log.e(TAG, "Error copying file to gallery: ${e.message}", e)
            onFail(e) // 复制文件失败，调用失败回调
        } catch (e: Exception) {
            Log.e(TAG, "An unexpected error occurred during file saving: ${e.message}", e)
            onFail(e) // 其他异常，调用失败回调
        }
    }

    /**
     * 根据 URL 生成文件名，尝试保留原始扩展名
     *
     * @param url 原始图片 URL
     * @return 生成的文件名
     */
    private fun generateFileName(url: String): String {
        // 尝试从 URL 获取文件扩展名
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        val fileExtension = if (extension.isNullOrEmpty()) {
            // 如果无法从 URL 获取扩展名，尝试从 MIME 类型获取（需要下载后才能确定 MIME 类型）
            // 或者默认使用 .jpg
            "jpg"
        } else {
            extension
        }

        // 使用当前时间戳作为文件名，加上扩展名
        return "${System.currentTimeMillis()}.${fileExtension.toLowerCase(Locale.getDefault())}"
    }


    /**
     * 复制文件
     * 使用 Kotlin 的 use 函数确保流自动关闭
     *
     * @param sourceFile 源文件
     * @param destFile 目标文件
     */
    @Throws(IOException::class) // 声明可能抛出 IOException
    private fun copyFile(sourceFile: File, destFile: File) {
        FileInputStream(sourceFile).use { input ->
            FileOutputStream(destFile).use { output ->
                // 使用 transferTo 提高复制效率 (Android 9+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    input.transferTo(output)
                } else {
                    // 对于旧版本，使用传统的读写方式
                    val buffer = ByteArray(1024)
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                }
                output.flush() // 确保所有数据写入文件
            }
        }
    }


    /**
     * 扫描媒体文件，使其出现在相册中
     *
     * @param context Context 对象
     * @param filePath 要扫描的文件路径
     * @param onScanCompleted 扫描完成回调 (可选)
     */
    private fun scanMediaFile(
        context: Context,
        filePath: String,
        onScanCompleted: (() -> Unit)? = null
    ) {
        MediaScannerConnection.scanFile(
            context,
            arrayOf(filePath), // 要扫描的文件路径数组
            null // MIME 类型数组，null 表示自动检测
        ) { path, uri ->
            // 扫描完成回调
            Log.d(TAG, "Scanned $path: -> uri=$uri")
            onScanCompleted?.invoke()
        }
    }
}
