package com.miaoyin.weiqi.other.utlis

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.io.*

/**
 * 图片工具类
 * 提供图片相关的操作，如加载、缩放、旋转、保存等
 */
object ImageUtils {

    private const val TAG = "ImageUtils"

    /**
     * 从指定路径加载 Bitmap
     * @param filePath 图片文件路径
     * @return 加载的 Bitmap，加载失败返回 null
     */
    fun loadBitmap(filePath: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(filePath)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading bitmap from file: $filePath", e)
            null
        }
    }

    /**
     * 从 Uri 加载 Bitmap
     * @param context Context 对象
     * @param uri 图片的 Uri
     * @return 加载的 Bitmap，加载失败返回 null
     */
    fun loadBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading bitmap from uri: $uri", e)
            null
        }
    }

    /**
     * 缩放 Bitmap
     * @param bitmap 原始 Bitmap
     * @param newWidth 目标宽度
     * @param newHeight 目标高度
     * @param filter 是否进行过滤（抗锯齿）
     * @return 缩放后的 Bitmap
     */
    fun scaleBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int, filter: Boolean = true): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, filter)
    }

    /**
     * 旋转 Bitmap
     * @param bitmap 原始 Bitmap
     * @param degrees 旋转角度 (0, 90, 180, 270)
     * @return 旋转后的 Bitmap
     */
    fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap? {
        if (degrees == 0f) return bitmap
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return try {
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: Exception) {
            Log.e(TAG, "Error rotating bitmap", e)
            null
        }
    }

    /**
     * 保存 Bitmap 到文件
     * @param bitmap 要保存的 Bitmap
     * @param filePath 保存的文件路径
     * @param format 保存格式 (PNG, JPEG, WEBP)
     * @param quality 图片质量 (0-100)
     * @return 是否成功保存
     */
    fun saveBitmap(
        bitmap: Bitmap,
        filePath: String,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 100
    ): Boolean {
        return try {
            val file = File(filePath)
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs() // 创建父目录
            }
            FileOutputStream(file).use { out ->
                bitmap.compress(format, quality, out)
                out.flush()
            }
            Log.d(TAG, "Bitmap saved to: $filePath")
            true
        } catch (e: IOException) {
            Log.e(TAG, "Error saving bitmap to file: $filePath", e)
            false
        }
    }

    /**
     * 获取图片文件的旋转角度 (Exif 信息)
     * @param filePath 图片文件路径
     * @return 旋转角度 (0, 90, 180, 270)，获取失败返回 0
     */
    fun getImageRotation(filePath: String): Int {
        return try {
            val exif = ExifInterface(filePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error getting image rotation from file: $filePath", e)
            0
        }
    }

    // TODO: 添加图片裁剪、灰度化、模糊等更多图片处理功能
    // TODO: 考虑使用 Glide 或 Coil 等图片加载库来处理更复杂的图片加载需求
}
