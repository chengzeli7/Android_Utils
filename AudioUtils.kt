package com.miaoyin.weiqi.other.utlis.new

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.util.Log
import java.io.IOException

/**
 * 音频工具类
 * 提供音频播放和录制相关的操作
 * 需要权限:
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> (如果录制到外部存储)
 */
object AudioUtils {

    private const val TAG = "AudioUtils"

    private var mediaPlayer: MediaPlayer? = null
    private var mediaRecorder: MediaRecorder? = null
    private var audioFilePath: String? = null

    /**
     * 播放音频文件
     * @param context Context 对象
     * @param filePath 音频文件路径
     * @param onCompletionListener 播放完成监听器 (可选)
     */
    fun playAudio(
        context: Context,
        filePath: String,
        onCompletionListener: MediaPlayer.OnCompletionListener? = null
    ) {
        stopAudio() // Stop any ongoing playback
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(filePath)
                prepare()
                setOnCompletionListener {
                    onCompletionListener?.onCompletion(it)
                    stopAudio() // Stop and release after completion
                }
                start()
                Log.d(TAG, "Started playing audio from file: $filePath")
            } catch (e: IOException) {
                Log.e(TAG, "Error playing audio from file: $filePath", e)
                release() // Release MediaPlayer on error
            }
        }
    }

    /**
     * 播放音频 Uri
     * @param context Context 对象
     * @param uri 音频 Uri
     * @param onCompletionListener 播放完成监听器 (可选)
     */
    fun playAudio(
        context: Context,
        uri: Uri,
        onCompletionListener: MediaPlayer.OnCompletionListener? = null
    ) {
        stopAudio() // Stop any ongoing playback
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(context, uri)
                prepare()
                setOnCompletionListener {
                    onCompletionListener?.onCompletion(it)
                    stopAudio() // Stop and release after completion
                }
                start()
                Log.d(TAG, "Started playing audio from uri: $uri")
            } catch (e: IOException) {
                Log.e(TAG, "Error playing audio from uri: $uri", e)
                release() // Release MediaPlayer on error
            }
        }
    }

    /**
     * 停止当前音频播放并释放资源
     */
    fun stopAudio() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
            Log.d(TAG, "Audio playback stopped and resources released.")
        }
        mediaPlayer = null
    }

    /**
     * 开始录制音频
     * @param filePath 音频文件保存路径
     * @return 是否成功开始录制
     */
    fun startRecording(context: Context, filePath: String): Boolean {
        if (mediaRecorder != null) {
            Log.w(TAG, "Recording is already in progress.")
            return false
        }

        audioFilePath = filePath
        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context) // Requires Context on API S+
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }

        mediaRecorder?.apply {
            try {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) // Or other suitable format
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // Or other suitable encoder
                setOutputFile(filePath)
                prepare()
                start()
                Log.d(TAG, "Started recording to file: $filePath")
                return true
            } catch (e: IOException) {
                Log.e(TAG, "Error starting recording to file: $filePath", e)
                release() // Release MediaRecorder on error
                mediaRecorder = null
                audioFilePath = null
                return false
            } catch (e: Exception) {
                Log.e(TAG, "Error configuring MediaRecorder", e)
                release() // Release MediaRecorder on error
                mediaRecorder = null
                audioFilePath = null
                return false
            }
        }
        return false
    }

    /**
     * 停止音频录制并释放资源
     * @return 录制文件的路径，如果录制未开始或停止失败则返回 null
     */
    fun stopRecording(): String? {
        mediaRecorder?.apply {
            try {
                stop()
                release()
                Log.d(TAG, "Audio recording stopped and resources released.")
                val recordedFilePath = audioFilePath
                audioFilePath = null
                mediaRecorder = null
                return recordedFilePath
            } catch (e: RuntimeException) {
                // This can happen if stop is called immediately after start
                Log.e(TAG, "Error stopping recording", e)
                release()
                mediaRecorder = null
                audioFilePath = null
                return null
            } catch (e: Exception) {
                Log.e(TAG, "Error releasing MediaRecorder", e)
                release()
                mediaRecorder = null
                audioFilePath = null
                return null
            }
        }
        Log.w(TAG, "No recording in progress to stop.")
        return null
    }

    /**
     * 获取音频文件的时长（毫秒）
     * @param filePath 音频文件路径
     * @return 音频时长（毫秒），获取失败返回 -1
     */
    fun getAudioDuration(filePath: String): Long {
        val retriever = android.media.MediaMetadataRetriever()
        return try {
            retriever.setDataSource(filePath)
            val duration =
                retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)
            duration?.toLong() ?: -1L
        } catch (e: Exception) {
            Log.e(TAG, "Error getting audio duration for file: $filePath", e)
            -1L
        } finally {
            retriever.release()
        }
    }

    // TODO: 添加音频文件的转换功能 (例如 AMR 转 MP3)
    // TODO: 考虑音频焦点管理
    // TODO: 考虑后台播放和录制
}
