package com.soli.change.newnotify.tools

import android.content.Context
import android.os.Build

object BaseTools {

    /**
     * 获取当前系统SDK版本号
     */
    /*获取当前系统的android版本号*/
    val systemVersion: Int
        get() = Build.VERSION.SDK_INT

    /**
     * 获取当前应用版本号
     * @param context
     * @return version
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getAppVersion(context: Context): String {
        // 获取packagemanager的实例
        val packageManager = context.packageManager
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        val packInfo = packageManager.getPackageInfo(context.packageName, 0)
        return packInfo.versionName
    }
}

