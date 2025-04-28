package com.miaoyin.weiqi.other.utlis

import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * 加密工具类
 * 提供一些常用的加密算法
 */
object EncryptUtils {

    private const val TAG = "EncryptUtils"

    /**
     * MD5 加密
     * @param data 要加密的字符串
     * @return MD5 加密后的字符串，如果发生错误则返回 null
     */
    fun encryptMD5(data: String): String? {
        return try {
            val md = MessageDigest.getInstance("MD5")
            md.update(data.toByteArray())
            bytesToHex(md.digest())
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "MD5 algorithm not found", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Error during MD5 encryption", e)
            null
        }
    }

    /**
     * SHA-256 加密
     * @param data 要加密的字符串
     * @return SHA-256 加密后的字符串，如果发生错误则返回 null
     */
    fun encryptSHA256(data: String): String? {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(data.toByteArray())
            bytesToHex(md.digest())
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "SHA-256 algorithm not found", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Error during SHA-256 encryption", e)
            null
        }
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xff
            hexChars[i * 2] = HEX_ARRAY[v ushr 4]
            hexChars[i * 2 + 1] = HEX_ARRAY[v and 0x0f]
        }
        return String(hexChars)
    }

    private val HEX_ARRAY = "0123456789abcdef".toCharArray()

    // TODO: 可以添加更多加密算法，例如 SHA-1, SHA-512, AES 等
    // TODO: 对于 AES 等对称加密，还需要考虑密钥管理
}
