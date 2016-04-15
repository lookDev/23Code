package com.boyou.autoservice.util.sysutil

import android.content.Context
import android.support.annotation.StringRes
import android.view.Gravity
import android.widget.Toast

/**
 * Created by Mrper on 16-1-10.
 * 提示框辅助类
 */
object ToastUtil {

    /**
     * 显示Toast提示框
     * @param context 上下文对象
     * @param message 提示信息
     */
    @JvmStatic fun showShortToast(context: Context, message: String) = Toast.makeText(context,message, Toast.LENGTH_SHORT).show()

    /**
     * 显示Toast提示框
     * @param context 上下文对象
     * @param mid     提示信息文本id
     */
    @JvmStatic fun showShortToast(context: Context, @StringRes mid: Int) = Toast.makeText(context,context.getString(mid), Toast.LENGTH_SHORT).show()

    /**
     * 显示Toast提示框
     * @param context 上下文对象
     * @param message 提示信息
     */
    @JvmStatic fun showLongToast(context: Context, message: String) = Toast.makeText(context,message, Toast.LENGTH_LONG).show()

    /**
     * 显示Toast提示框
     * @param context 上下文对象
     * @param mid     提示信息文本id
     */
    @JvmStatic fun showLongToast(context: Context,@StringRes mid: Int) = Toast.makeText(context,context.getString(mid), Toast.LENGTH_LONG).show()


    /**
     * 显示Toast提示框
     * @param context  上下文对象
     * @param mid      提示信息文字ID
     * @param duration 显示时长
     * @param gravity  显示位置
     * @param x        位置X偏移量
     * @param y        位置Y偏移量
     */
    @JvmStatic fun showToast(context: Context,@StringRes mid: Int,  duration: Int = Toast.LENGTH_SHORT, gravity: Int = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, x: Int = 0, y: Int = DensityUtil.dip2px(context,64f)): Unit {
        showToast(context,context.getString(mid),duration,gravity,x,y)
    }

    /**
     * 显示Toast提示框
     * @param context 上下文对象
     * @param message 提示信息
     * @param duration1 显示时长
     * @param gravity 显示位置
     * @param x       位置X偏移量
     * @param y       位置Y偏移量
     */
    @JvmStatic fun showToast(context: Context, message: String, duration1: Int = Toast.LENGTH_SHORT, gravity: Int = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, x: Int = 0, y: Int = DensityUtil.dip2px(context,64f)): Unit {
        var toast: Toast = Toast.makeText(context, message, duration1)
        with(toast){
            duration = duration1
            setGravity(gravity, x, y)
            show()
        }
    }

}