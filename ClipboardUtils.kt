package com.miaoyin.weiqi.other.utlis

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log

/**
 * 剪贴板工具类
 */
object ClipboardUtils {

    private const val TAG = "ClipboardUtils"

    /**
     * 复制文本到剪贴板
     * @param context Context 对象
     * @param text 要复制的文本
     */
    fun copyText(context: Context, text: CharSequence?) {
        if (text == null) {
            Log.w(TAG, "Attempted to copy null text to clipboard.")
            return
        }
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
        Log.d(TAG, "Copied text to clipboard: $text")
    }

    /**
     * 从剪贴板获取文本
     * @param context Context 对象
     * @return 剪贴板中的文本，如果剪贴板为空或内容不是文本则返回 null
     */
    fun pasteText(context: Context): CharSequence? {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboardManager.primaryClip ?: return null
        if (clipData.itemCount > 0) {
            val item = clipData.getItemAt(0)
            // Use coerceToText to get the text representation, handling different types
            val text = item.coerceToText(context)
            Log.d(TAG, "Pasted text from clipboard: $text")
            return text
        }
        Log.d(TAG, "Clipboard is empty or does not contain text.")
        return null
    }

    /**
     * 复制图片到剪贴板
     * 注意：复制图片通常需要读写外部存储权限，并且在 Android 10+ 有存储访问限制。
     * 更推荐的方式是复制图片的 Uri，而不是直接复制 Bitmap。
     * @param context Context 对象
     * @param bitmap 要复制的图片 Bitmap
     */
    fun copyImage(context: Context, bitmap: Bitmap?) {
        if (bitmap == null) {
            Log.w(TAG, "Attempted to copy null bitmap to clipboard.")
            return
        }
        // Note: Directly copying Bitmap to clipboard is less common and might have limitations.
        // Copying a file URI or content URI is generally preferred.
        // This implementation is a basic example and might not work in all scenarios.
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // Creating a ClipData for an image is more complex and usually involves a Uri.
        // A simplified approach might involve saving the bitmap to a temporary file and copying its Uri.
        // For a robust solution, consider using a ContentProvider to share the image.
        // This basic example will not directly copy the Bitmap.
        Log.w(TAG, "Directly copying Bitmap to clipboard is complex and not fully implemented here. Consider copying a Uri instead.")
        // Example of how you might start to create a ClipData for an image URI:
        // val imageUri: Uri? = saveBitmapToTemporaryFile(context, bitmap) // You would need to implement this
        // if (imageUri != null) {
        //     val clipData = ClipData.newUri(context.contentResolver, "image", imageUri)
        //     clipboardManager.setPrimaryClip(clipData)
        //     Log.d(TAG, "Copied image URI to clipboard: $imageUri")
        // } else {
        //     Log.e(TAG, "Failed to get image URI for copying.")
        // }
    }

    /**
     * 从剪贴板获取图片 URI
     * @param context Context 对象
     * @return 剪贴板中的图片 URI，如果剪贴板为空或内容不是图片 URI 则返回 null
     */
    fun pasteImageUri(context: Context): Uri? {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboardManager.primaryClip ?: return null
        if (clipData.itemCount > 0) {
            val item = clipData.getItemAt(0)
            val uri = item.uri
            if (uri != null && (uri.scheme == "content" || uri.scheme == "file")) {
                 // Basic check if it looks like an image URI (you might need more robust checks)
                 if (context.contentResolver.getType(uri)?.startsWith("image/") == true) {
                     Log.d(TAG, "Pasted image URI from clipboard: $uri")
                     return uri
                 }
            }
        }
        Log.d(TAG, "Clipboard does not contain a valid image URI.")
        return null
    }


    /**
     * 复制 URI 到剪贴板
     * @param context Context 对象
     * @param uri 要复制的 URI
     */
    fun copyUri(context: Context, uri: Uri?) {
        if (uri == null) {
            Log.w(TAG, "Attempted to copy null URI to clipboard.")
            return
        }
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newUri(context.contentResolver, "uri", uri)
        clipboardManager.setPrimaryClip(clipData)
        Log.d(TAG, "Copied URI to clipboard: $uri")
    }

    /**
     * 从剪贴板获取 URI
     * @param context Context 对象
     * @return 剪贴板中的 URI，如果剪贴板为空或内容不是 URI 则返回 null
     */
    fun pasteUri(context: Context): Uri? {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboardManager.primaryClip ?: return null
        if (clipData.itemCount > 0) {
            val item = clipData.getItemAt(0)
            val uri = item.uri
            if (uri != null) {
                Log.d(TAG, "Pasted URI from clipboard: $uri")
                return uri
            }
        }
        Log.d(TAG, "Clipboard does not contain a URI.")
        return null
    }
}
