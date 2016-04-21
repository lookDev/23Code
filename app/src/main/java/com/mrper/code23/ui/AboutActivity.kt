package com.mrper.code23.ui

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.format.Formatter
import com.mrper.code23.R
import com.mrper.code23.fewk.annotation.BackAction
import com.mrper.code23.fewk.annotation.ContentView
import com.mrper.code23.fewk.ui.BaseActivity
import com.mrper.code23.fewk.utils.ActivityUtil
import com.mrper.code23.fewk.utils.ApkUtil
import com.mrper.code23.fewk.utils.CacheUtil
import com.mrper.code23.fewk.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_about.*
import java.lang.ref.WeakReference

@BackAction
@ContentView(R.layout.activity_about)
class AboutActivity : BaseActivity() {

    private var cacheUtil: CacheUtil = CacheUtil.getInstance()
    private var cacheHandler: CacheHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(toolbar)
        cacheHandler = CacheHandler(this)
        txtVersion.text = ApkUtil.getVersionName(this)
        //点击事件
        btnUpgrade.setOnClickListener {  }
        btnClearCache.setOnClickListener { CacheThread(this,TAG_CLEAR_CACHE_DONE).start() }
        //获取缓存信息
        CacheThread(this,TAG_GET_CACHE_SIZE).start()
    }

    override fun onToolbarNavigationClicked() {
        super.onToolbarNavigationClicked()
        ActivityUtil.closeActivity(this)
    }

    companion object {

        @JvmField val TAG_GET_CACHE_SIZE = 0x001
        @JvmField val TAG_CLEAR_CACHE_DONE = 0x002

        class CacheThread(val activity: AboutActivity?,val tag: Int) : Thread(){

            override fun run() {
                super.run()
                when(tag){
                    TAG_GET_CACHE_SIZE -> {
                        val cacheSize = activity?.cacheUtil?.getCacheSize()
                        activity?.cacheHandler?.obtainMessage(MESSAGE_GET_CACHE_SIZE,cacheSize)?.sendToTarget()
                    }
                    TAG_CLEAR_CACHE_DONE -> {
                        activity?.cacheUtil?.clearCache{}
                        activity?.cacheHandler?.sendEmptyMessage(MESSAGE_CLEAR_CACHE_DONE)
                    }
                }

            }

        }

        @JvmField val MESSAGE_GET_CACHE_SIZE = 0x001
        @JvmField val MESSAGE_CLEAR_CACHE_DONE = 0x002

        class CacheHandler(val activity: AboutActivity?) : Handler() {

            private var activityRef: WeakReference<AboutActivity?>? = null

            init{
                activityRef = WeakReference(activity)
            }

            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                val activity = activityRef?.get()
                if(activity!=null){
                    when(msg?.what){
                        MESSAGE_GET_CACHE_SIZE -> {
                            val cacheSize = msg?.obj.toString().toLong()
                            activity.txtCacheInfo.text = if(cacheSize > 0L) Formatter.formatFileSize(activity,cacheSize) else "无需清理"
                            activity.btnClearCache.isEnabled = cacheSize > 0L
                        }
                        MESSAGE_CLEAR_CACHE_DONE -> {
                            ToastUtil.showShortToast(activity,"缓存清理完成")
                            activity.txtCacheInfo.text = "无需清理"
                        }
                    }
                }
            }

        }

    }

}
