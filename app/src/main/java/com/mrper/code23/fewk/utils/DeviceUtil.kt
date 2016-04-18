package com.mrper.code23.fewk.utils

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.Uri
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by Mrper on 15-12-31.
 * 设备辅助类
 */
object DeviceUtil {

    /**
     * 获取屏幕信息
     * @param context 上下文对象
     */
    @JvmStatic fun getScreenInfo(context: Context): DisplayMetrics? {
        var dm: DisplayMetrics? = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        return dm
    }

    /**
     * 获取屏幕宽度
     * @param context  上下文对象
     */
    @JvmStatic fun getScreenWidth(context: Context): Int = DeviceUtil.getScreenInfo(context)!!.widthPixels

    /**
     * 获取屏幕高度
     * @param context 上下文对象
     */
    @JvmStatic fun getScreenHeight(context: Context): Int = DeviceUtil.getScreenInfo(context)!!.heightPixels

    /**
     * 获取状态栏高度
     * @param context 上下文对象
     */
    @JvmStatic fun getStatusBarHeight(context: Context): Int {
        try{
            val cls = Class.forName("com.android.internal.R${'$'}dimen")
            val obj = cls.newInstance()
            val field = cls.getField("status_bar_height")
            val id: Int = field.getInt(obj)
            return context.resources.getDimensionPixelSize(id)
        }catch(e: Exception){
            e.printStackTrace()
        }
        return DensityUtil.dip2px(context,25f)
    }


    /**
     * 获取电话号码
     * @param context 上下文对象
     */
    @JvmStatic fun getPhoneNumber(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.line1Number
    }

    /**
     * 获取电话唯一标识（IMEI）
     * @param context 上下文对象
     */
    @JvmStatic fun getPhoneIMEI(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.deviceId
    }

    /**
     * 检测当前网络是否可用
     * @param context 上下文对象
     */
    @JvmStatic fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if(cm.activeNetworkInfo!=null) cm.activeNetworkInfo.isAvailable else false
    }

    /**
     * 获取当前网络可用类型
     * @param context 上下文对象
     */
    @JvmStatic fun getAvailableNetworkType(context: Context): Int {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if(cm.activeNetworkInfo!=null) cm.activeNetworkInfo.type else -1
    }

    /**
     * 直接拨打电话
     * @param context 上下文对象
     * @param phoneNumber 电话号码
     */
    @JvmStatic fun callPhoneNumber(context: Context,phoneNumber: String): Unit {
        var intent: Intent = Intent(Intent.ACTION_CALL)
        intent.setData(Uri.parse("tel:$phoneNumber"))
        context.startActivity(intent)
    }

    /**
     * 显示拨号界面
     * @param context 上下文对象
     * @param phoneNumber 电话号码
     */
    @JvmStatic fun showCallDial(context: Context,phoneNumber: String): Unit {
        var intent: Intent = Intent(Intent.ACTION_DIAL)
        intent.setData(Uri.parse("tel:$phoneNumber"))
        context.startActivity(intent)
    }

    /**  获取字体宽度  **/
    @JvmStatic fun getFontMetricsWidth(paint: Paint, text:String): Float = paint.measureText(text)

    /**  获取字体高度 **/
    @JvmStatic fun getFontMetricsHeight(paint: Paint): Float
            = Math.ceil(paint.fontMetrics.descent.toDouble() - paint.fontMetrics.ascent).toFloat()

}