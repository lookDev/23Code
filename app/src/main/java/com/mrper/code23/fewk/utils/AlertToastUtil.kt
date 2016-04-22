package com.mrper.code23.fewk.utils

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast

/**
 * Created by admin on 2016/4/22.
 */
object AlertToastUtil{

    private var originalMessage: String? = null

    private var toast: Toast? = null

    private var time1: Long = 0L

    private var time2: Long = 0L

    fun showToast(context: Context,message: String){
        if(toast == null){
            toast = Toast.makeText(context,message,Toast.LENGTH_SHORT)
            toast?.show()
            time1 = System.currentTimeMillis()
        }else{
            time2 = System.currentTimeMillis()
            if(message.equals(originalMessage)){
                if(time2 - time1 > Toast.LENGTH_SHORT)
                    toast?.show()
            }else{
                originalMessage = message
                toast?.setText(message)
                toast?.show()
            }
        }
        time1 = time2
    }

    fun showToast(context: Context,@StringRes resId: Int)
            = showToast(context,context.getString(resId))

}