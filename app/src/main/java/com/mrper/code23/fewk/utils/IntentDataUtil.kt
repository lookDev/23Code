package com.mrper.code23.fewk.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * Created by Mrper on 16-1-15.
 * 针对Intent内extras数据的获取辅助类
 */
class IntentDataUtil(val activity: Activity) {

    companion object {

        /**  获取Intent数据包操作对象,非单例  **/
        @JvmStatic fun getInstance(activity: Activity) = IntentDataUtil(activity)

    }

    private var intent: Intent? = null

    private var data: Bundle? = null

    init{
        intent = activity.intent
        data = intent?.extras
    }

    /**  是否有数据  **/
    val isHasData: Boolean
        get() = (intent != null && data != null)

    /**  是否包含某个key-value  **/
    fun contains(key: String): Boolean = data?.containsKey(key) ?: false

    /**
     * 根据KEY获取data中的数据
     * @param key 键值
     */
    fun <T> getValue(key: String,default: T): T {
        val ret = @Suppress("UNCHECKED_CAST")(data?.get(key) as? T)
        return ret?:default
    }

    fun <T> getValue(key: String): T? = @Suppress("UNCHECKED_CAST")(data?.get(key) as? T)

}