package com.lv.gankio

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.lv.gankio.util.DLog
import com.pgyersdk.crash.PgyCrashManager

/**
 * Date: 2017-06-22
 * Time: 17:04
 * Description:
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
        DLog.init()
        PgyCrashManager.register(this)
    }
}