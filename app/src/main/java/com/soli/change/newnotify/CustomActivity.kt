package com.soli.change.newnotify

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.Builder
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.RemoteViews
import android.widget.Toast
import com.soli.change.newnotify.base.BaseActivity
import com.soli.change.newnotify.tools.BaseTools
import kotlinx.android.synthetic.main.custom.*

class CustomActivity : BaseActivity(), OnClickListener {
    /**
     * Notification 的ID
     */
    var notifyId = 101
    /**
     * NotificationCompat 构造器
     */
    var mBuilder: NotificationCompat.Builder? = null
    /**
     * 是否在播放
     */
    var isPlay = false
    /**
     * 通知栏按钮广播
     */
    var bReceiver: ButtonBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom)
        initView()
        initButtonReceiver()
    }

    private fun initView() {
        btn_show_custom!!.setOnClickListener(this)
        btn_show_custom_button!!.setOnClickListener(this)
    }

    fun shwoNotify() {
        //先设定RemoteViews
        val view_custom = RemoteViews(packageName, R.layout.view_custom)
        //设置对应IMAGEVIEW的ID的资源图片
        view_custom.setImageViewResource(R.id.custom_icon, R.mipmap.icon)
        //		view_custom.setInt(R.id.custom_icon,"setBackgroundResource",R.mipmap.icon);
        view_custom.setTextViewText(R.id.tv_custom_title, "今日头条")
        view_custom.setTextViewText(R.id.tv_custom_content, "金州勇士官方宣布球队已经解雇了主帅马克-杰克逊，随后宣布了最后的结果。")
        //		view_custom.setTextViewText(R.id.tv_custom_time, String.valueOf(System.currentTimeMillis()));
        //设置显示
        //		view_custom.setViewVisibility(R.id.tv_custom_time, View.VISIBLE);
        //		view_custom.setLong(R.id.tv_custom_time,"setTime", System.currentTimeMillis());//不知道为啥会报错，过会看看日志
        //设置number
        //		NumberFormat num = NumberFormat.getIntegerInstance();
        //		view_custom.setTextViewText(R.id.tv_custom_num, num.format(this.number));
        mBuilder = Builder(this)
        mBuilder!!.setContent(view_custom)
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setTicker("有新资讯")
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setOngoing(false)//不是正在进行的   true为正在进行  效果和.flag一样
                .setSmallIcon(R.mipmap.icon)
        //		mNotificationManager.notify(notifyId, mBuilder.build());
        val notify = mBuilder!!.build()
        notify.contentView = view_custom
        //		Notification notify = new Notification();
        //		notify.icon = R.mipmap.icon;
        //		notify.contentView = view_custom;
        //		notify.contentIntent = getDefalutIntent(Notification.FLAG_AUTO_CANCEL);
        mNotificationManager.notify(notifyId, notify)
    }

    /**
     * 带按钮的通知栏
     */
    fun showButtonNotify() {
        val mBuilder = Builder(this)
        val mRemoteViews = RemoteViews(packageName, R.layout.view_custom_button)
        mRemoteViews.setImageViewResource(R.id.custom_song_icon, R.mipmap.sing_icon)
        //API3.0 以上的时候显示按钮，否则消失
        mRemoteViews.setTextViewText(R.id.tv_custom_song_singer, "周杰伦")
        mRemoteViews.setTextViewText(R.id.tv_custom_song_name, "七里香")
        //如果版本号低于（3。0），那么不显示按钮
        if (BaseTools.systemVersion <= 9) {
            mRemoteViews.setViewVisibility(R.id.ll_custom_button, View.GONE)
        } else {
            mRemoteViews.setViewVisibility(R.id.ll_custom_button, View.VISIBLE)
            //
            if (isPlay) {
                mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.mipmap.btn_pause)
            } else {
                mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.mipmap.btn_play)
            }
        }

        //点击的事件处理
        val buttonIntent = Intent(ACTION_BUTTON)
        /* 上一首按钮 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PREV_ID)
        //这里加了广播，所及INTENT的必须用getBroadcast方法
        val intent_prev = PendingIntent.getBroadcast(this, 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_prev, intent_prev)
        /* 播放/暂停  按钮 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID)
        val intent_paly = PendingIntent.getBroadcast(this, 2, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_play, intent_paly)
        /* 下一首 按钮  */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_NEXT_ID)
        val intent_next = PendingIntent.getBroadcast(this, 3, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_next, intent_next)

        mBuilder!!.setContent(mRemoteViews)
                .setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT))
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setTicker("正在播放")
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setOngoing(true)
                .setSmallIcon(R.mipmap.sing_icon)
        val notify = mBuilder!!.build()
        notify.flags = Notification.FLAG_ONGOING_EVENT
        //会报错，还在找解决思路
        //		notify.contentView = mRemoteViews;
        //		notify.contentIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        mNotificationManager.notify(200, notify)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_show_custom -> shwoNotify()
            R.id.btn_show_custom_button -> showButtonNotify()
            else -> {
            }
        }
    }

    /**
     * 带按钮的通知栏点击广播接收
     */
    fun initButtonReceiver() {
        bReceiver = ButtonBroadcastReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_BUTTON)
        registerReceiver(bReceiver, intentFilter)
    }

    /**
     * 广播监听按钮点击时间
     */
    inner class ButtonBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == ACTION_BUTTON) {
                //通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
                val buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0)
                when (buttonId) {
                    BUTTON_PREV_ID -> {
                        Log.d(TAG, "上一首")
                        Toast.makeText(applicationContext, "上一首", Toast.LENGTH_SHORT).show()
                    }
                    BUTTON_PALY_ID -> {
                        var play_status = ""
                        isPlay = !isPlay
                        if (isPlay) {
                            play_status = "开始播放"
                        } else {
                            play_status = "已暂停"
                        }
                        showButtonNotify()
                        Log.d(TAG, play_status)
                        Toast.makeText(applicationContext, play_status, Toast.LENGTH_SHORT).show()
                    }
                    BUTTON_NEXT_ID -> {
                        Log.d(TAG, "下一首")
                        Toast.makeText(applicationContext, "下一首", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        if (bReceiver != null) {
            unregisterReceiver(bReceiver)
        }
        super.onDestroy()
    }

    companion object {
        /**
         * TAG
         */
        private val TAG = "CustomActivity"
        /**
         * 通知栏按钮点击事件对应的ACTION
         */
        val ACTION_BUTTON = "com.notifications.intent.action.ButtonClick"

        val INTENT_BUTTONID_TAG = "ButtonId"
        /**
         * 上一首 按钮点击 ID
         */
        val BUTTON_PREV_ID = 1
        /**
         * 播放/暂停 按钮点击 ID
         */
        val BUTTON_PALY_ID = 2
        /**
         * 下一首 按钮点击 ID
         */
        val BUTTON_NEXT_ID = 3
    }
}
