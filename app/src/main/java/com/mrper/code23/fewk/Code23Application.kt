package com.mrper.code23.fewk

import android.app.Application
import com.mrper.crashcatcher.main.CatcherHandler

/**
 * Created by admin on 2016/4/25.
 */
class Code23Application : Application(){

    override fun onCreate() {
        super.onCreate()
        CatcherHandler().init(this)
    }
}