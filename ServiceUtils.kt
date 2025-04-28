package com.miaoyin.weiqi.other.utlis.new

import android.app.Service
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Service 工具类
 * 方便启动和停止 Service
 */
object ServiceUtils {

    private const val TAG = "ServiceUtils"

    /**
     * 启动 Service
     * @param context Context 对象
     * @param serviceClass 要启动的 Service 类
     * @param extras 传递给 Service 的额外数据 (可选)
     */
    fun startService(
        context: Context,
        serviceClass: Class<out Service>,
        extras: android.os.Bundle? = null
    ) {
        try {
            val intent = Intent(context, serviceClass)
            extras?.let { intent.putExtras(it) }
            context.startService(intent)
            Log.d(TAG, "Started Service: ${serviceClass.simpleName}")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting Service: ${serviceClass.simpleName}", e)
        }
    }

    /**
     * 停止 Service
     * @param context Context 对象
     * @param serviceClass 要停止的 Service 类
     * @return 如果 Service 正在运行并被停止，则返回 true；否则返回 false
     */
    fun stopService(context: Context, serviceClass: Class<out Service>): Boolean {
        return try {
            val intent = Intent(context, serviceClass)
            val stopped = context.stopService(intent)
            if (stopped) {
                Log.d(TAG, "Stopped Service: ${serviceClass.simpleName}")
            } else {
                Log.d(TAG, "Service not running or failed to stop: ${serviceClass.simpleName}")
            }
            stopped
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping Service: ${serviceClass.simpleName}", e)
            false
        }
    }

    // TODO: 添加绑定 Service 的方法
    // TODO: 添加检查 Service 是否正在运行的方法
}
