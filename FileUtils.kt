package com.miaoyin.weiqi.other.utlis

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat

/**
 * 文件工具类
 */
object FileUtils {

    private const val TAG = "FileUtils"
    private const val BYTE = 1024.0
    private const val KB = BYTE * 1024
    private const val MB = KB * 1024
    private const val GB = MB * 1024

    /**
     * 获取外部存储的根目录
     * @return 外部存储根目录的 File 对象，如果不可用则返回 null
     */
    fun getExternalStorageRootDirectory(): File? {
        return if (isExternalStorageWritable()) {
            Environment.getExternalStorageDirectory()
        } else {
            Log.w(TAG, "External storage is not writable.")
            null
        }
    }

    /**
     * 检查外部存储是否可写
     * @return 外部存储是否可写
     */
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * 检查外部存储是否可读
     * @return 外部存储是否可读
     */
    fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
    }

    /**
     * 创建目录
     * @param dirPath 目录路径
     * @return 是否成功创建目录
     */
    fun createDir(dirPath: String): Boolean {
        val dir = File(dirPath)
        return if (!dir.exists()) {
            val success = dir.mkdirs()
            if (success) {
                Log.d(TAG, "Directory created: $dirPath")
            } else {
                Log.e(TAG, "Failed to create directory: $dirPath")
            }
            success
        } else {
            Log.d(TAG, "Directory already exists: $dirPath")
            true // Directory already exists, consider it successful
        }
    }

    /**
     * 创建文件
     * @param filePath 文件路径
     * @return 是否成功创建文件
     */
    fun createFile(filePath: String): Boolean {
        val file = File(filePath)
        return if (!file.exists()) {
            try {
                val success = file.createNewFile()
                if (success) {
                    Log.d(TAG, "File created: $filePath")
                } else {
                    Log.e(TAG, "Failed to create file: $filePath")
                }
                success
            } catch (e: IOException) {
                Log.e(TAG, "Error creating file: $filePath", e)
                false
            }
        } else {
            Log.d(TAG, "File already exists: $filePath")
            true // File already exists, consider it successful
        }
    }

    /**
     * 删除文件或目录
     * @param file 要删除的文件或目录
     * @return 是否成功删除
     */
    fun deleteFile(file: File?): Boolean {
        if (file == null || !file.exists()) {
            Log.w(TAG, "File does not exist or is null.")
            return false
        }

        if (file.isFile) {
            val deleted = file.delete()
            if (deleted) {
                Log.d(TAG, "File deleted: ${file.absolutePath}")
            } else {
                Log.e(TAG, "Failed to delete file: ${file.absolutePath}")
            }
            return deleted
        }

        if (file.isDirectory) {
            val children = file.listFiles()
            if (children != null) {
                for (child in children) {
                    // Recursively delete children
                    if (!deleteFile(child)) {
                        Log.e(TAG, "Failed to delete child: ${child.absolutePath}")
                        return false // If any child deletion fails, return false
                    }
                }
            }
            // Delete the directory itself after deleting its children
            val deleted = file.delete()
            if (deleted) {
                Log.d(TAG, "Directory deleted: ${file.absolutePath}")
            } else {
                Log.e(TAG, "Failed to delete directory: ${file.absolutePath}")
            }
            return deleted
        }

        return false // Should not reach here
    }

    /**
     * 删除文件或目录
     * @param filePath 要删除的文件或目录路径
     * @return 是否成功删除
     */
    fun deleteFile(filePath: String): Boolean {
        return deleteFile(File(filePath))
    }

    /**
     * 复制文件
     * @param sourceFile 源文件
     * @param destFile 目标文件
     * @return 是否成功复制
     */
    fun copyFile(sourceFile: File, destFile: File): Boolean {
        if (!sourceFile.exists()) {
            Log.e(TAG, "Source file does not exist: ${sourceFile.absolutePath}")
            return false
        }
        if (destFile.exists()) {
            Log.w(TAG, "Destination file already exists, overwriting: ${destFile.absolutePath}")
            deleteFile(destFile) // Optionally delete if exists
        }

        try {
            FileInputStream(sourceFile).use { `in` ->
                FileOutputStream(destFile).use { out ->
                    val buffer = ByteArray(1024)
                    var read: Int
                    while (`in`.read(buffer).also { read = it } != -1) {
                        out.write(buffer, 0, read)
                    }
                    out.flush()
                    Log.d(TAG, "File copied from ${sourceFile.absolutePath} to ${destFile.absolutePath}")
                    return true
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error copying file", e)
            return false
        }
    }

    /**
     * 复制文件
     * @param sourcePath 源文件路径
     * @param destPath 目标文件路径
     * @return 是否成功复制
     */
    fun copyFile(sourcePath: String, destPath: String): Boolean {
        return copyFile(File(sourcePath), File(destPath))
    }

    /**
     * 获取文件大小
     * @param file 文件
     * @return 文件大小（字节），如果文件不存在或为目录则返回 0
     */
    fun getFileSize(file: File?): Long {
        if (file == null || !file.exists() || file.isDirectory) {
            return 0
        }
        return file.length()
    }

    /**
     * 获取文件大小
     * @param filePath 文件路径
     * @return 文件大小（字节），如果文件不存在或为目录则返回 0
     */
    fun getFileSize(filePath: String): Long {
        return getFileSize(File(filePath))
    }

    /**
     * 格式化文件大小，例如 10MB, 2.5GB
     * @param fileSize 文件大小（字节）
     * @return 格式化后的文件大小字符串
     */
    fun formatFileSize(fileSize: Long): String {
        val df = DecimalFormat("#.00")
        return when {
            fileSize < BYTE -> "$fileSize B"
            fileSize < KB -> df.format(fileSize / BYTE) + " KB"
            fileSize < MB -> df.format(fileSize / KB) + " MB"
            fileSize < GB -> df.format(fileSize / MB) + " GB"
            else -> df.format(fileSize / GB) + " TB"
        }
    }

    // TODO: 可以添加更多文件相关的操作，例如读取写入文件内容、获取文件扩展名等
}
