package com.boyou.autoservice.util

import com.mrper.code23.fewk.annotation.ContentView
import java.util.regex.Matcher
import java.util.regex.Pattern

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


    /**
     * 正则表达式匹配
     * @param pattern
     * @param input
     */
    @JvmStatic fun regexMatcher(pattern: String,input: String): Matcher = Pattern.compile(pattern).matcher(input)


}