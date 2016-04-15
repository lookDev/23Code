package com.boyou.autoservice.util

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.ListAdapter
import android.widget.TextView
import com.mrper.code23.fewk.annotation.ContentView
import java.io.File

/**
 * Created by Mrper on 15-12-9.
 * 公共方法辅助方法
 */

object CommonUtil {

    /**
     * 根据实例对象获取布局ID，针对带ContentView注解的类
     * @param instance fragment或activity实例对象
     */
    @JvmStatic fun getLayoutId(instance: Any): Int = instance.javaClass.getAnnotation(ContentView::class.java)?.value ?: 0

}