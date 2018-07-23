package com.soli.change.newnotify.base


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    /**
     * Notification管理
     */
    open lateinit var mNotificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initService()
    }

    /**
     * 初始化要用到的系统服务
     */
    private fun initService() {
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /**
     * 清除当前创建的通知栏
     */
    fun clearNotify(notifyId: Int) {
        mNotificationManager.cancel(notifyId)//删除一个特定的通知ID对应的通知
        //		mNotification.cancel(getResources().getString(R.string.app_name));
    }

    /**
     * 清除所有通知栏
     */
    fun clearAllNotify() {
        mNotificationManager.cancelAll()// 删除你发的所有通知
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    fun getDefalutIntent(flags: Int): PendingIntent {
        return PendingIntent.getActivity(this, 1, Intent(), flags)
    }
}