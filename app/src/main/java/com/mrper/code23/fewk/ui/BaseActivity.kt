package com.mrper.code23.fewk.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import com.boyou.autoservice.util.CommonUtil
import com.boyou.autoservice.util.sysutil.ApkUtil
import com.mrper.code23.R
import com.mrper.code23.fewk.annotation.BackAction
import com.readystatesoftware.systembartint.SystemBarTintManager

/**
 * Created by Mrper on 15-12-9.
 * Activity基类，其他activity需要继承该activity类
 */
open class BaseActivity : AppCompatActivity() {

    protected lateinit var context: Activity//上下文对象

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.context = this@BaseActivity
        if (isHideSystemBar) {
            //如果不隐藏系统状态栏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                ApkUtil.setTranslucentStatusBar(this, true)
            val tintManager = SystemBarTintManager(this)
            tintManager.isStatusBarTintEnabled = true
            tintManager.setStatusBarTintResource(systemBarTintResource)//通知栏所需颜色
        }
        setContentView(CommonUtil.getLayoutId(this))
    }

    /**  是否隐藏系统状态栏   */
    protected val isHideSystemBar: Boolean
        get() = true

    /**  获取顶部system bar 的颜色   */
    protected val systemBarTintResource: Int
        get() = R.color.colorPrimaryDark

    /**
     * 如果页面带有toolbar控件
     * @param toolbar
     */
    protected fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { v -> onToolbarNavigationClicked() }
    }

    /** 页面上的toolbar的导航图标被点击时执行   */
    protected open fun onToolbarNavigationClicked() {
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            val bae = javaClass.getAnnotation(BackAction::class.java)
            if (bae != null) {
                this.finish()//关闭当前页面
                overridePendingTransition(bae.enterAnimationId, bae.exitAnimationId)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

//    @JvmField val httpCallback = object : AsyncHttpResponseHandler() {
//
//        override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
//            handleHttpResponse(statusCode,responseBody?.toString()!!)
//        }
//
//        override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
//            ToastUtil.showShortToast(this@BaseActivity,"数据加载失败!")
//        }
//
//    }
//
//    protected open fun handleHttpResponse(statusCode: Int,result: String){
//
//    }

}
