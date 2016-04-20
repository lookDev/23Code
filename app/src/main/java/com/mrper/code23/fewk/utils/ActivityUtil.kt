package com.mrper.code23.fewk.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.mrper.code23.R

/**
 * Created by Mrper on 16-1-11.
 * activity辅助类
 */
object ActivityUtil {

    /**
     * 跳转至另一个activity
     * @param context 上下文对象
     * @param jclass 要跳转去的activity类
     * @param finishCur 是否关闭当前activity
     * @param data 携带的数据包，默认为：null
     * @param requestCode 请求码，为0则不使用,默认为：0
     * @param backData 界面返回携带的数据
     * @param resultCode 结果码
     * @param enterAnimRes 进入页面动画Res ID,默认值为：R.anim.slide_in_right
     * @param exitAnimRes 退出页面动画Res ID，默认值为：R.anim.slide_out_left
     */
    @JvmStatic
    @JvmOverloads
    fun goForward(context: Context, jclass: Class<*>, finishCur: Boolean, data: Bundle? = null, requestCode: Int = 0,backData: Intent? = null,resultCode:Int = Activity.RESULT_CANCELED, enterAnimRes: Int = R.anim.slide_in_right, exitAnimRes: Int = R.anim.slide_out_left): Unit {
        val intent = Intent(context, jclass)
        if (data != null) intent.putExtras(data)
        val activity = context as Activity
        with(activity) {
            if (requestCode != 0) {
                startActivityForResult(intent, requestCode)
            } else {
                startActivity(intent)
            }
            overridePendingTransition(enterAnimRes, exitAnimRes)
            if (finishCur){
                if(resultCode != Activity.RESULT_CANCELED){//如果有返回结果
                    if(backData == null)
                        setResult(resultCode)
                    else
                        setResult(resultCode,backData)
                }
                finish()
            }
        }
    }

    /**
     * 关闭当前页面
     * @param activity
     * @param resultCode 请求结果码，为0是【无效】默认为：0
     * @param data 携带数据，默认为：null
     * @param enterAnimRes 进入页面动画Res ID,默认为：android.R.anim.slide_in_left
     * @param exitAnimRes 退出页面动画Res ID，默认为：android.R.anim.slide_out_right
     */
    @JvmStatic
    @JvmOverloads
    fun closeActivity(activity: Activity, resultCode: Int = 0, data: Bundle? = null, enterAnimRes: Int = android.R.anim.slide_in_left, exitAnimRes: Int = android.R.anim.slide_out_right): Unit = with(activity) {
        if (resultCode != 0) {
            if (data != null) {
                val datIntent = Intent()
                datIntent.putExtras(data)
                setResult(resultCode, datIntent)
            } else {
                setResult(resultCode)
            }
        }
        finish()
        overridePendingTransition(enterAnimRes, exitAnimRes)
    }


    /**  退出应用程序 **/
    @JvmStatic fun exitApplication(): Unit = try {
        System.exit(0)//退出JVM
        android.os.Process.killProcess(android.os.Process.myPid())
        android.os.Process.killProcess(android.os.Process.myTid())
        android.os.Process.killProcess(android.os.Process.myUid())
    } catch(e: Exception) {
        e.printStackTrace()
    }

    /** 打开浏览器 **/
    @JvmStatic fun openBrowser(context: Context,url: String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }


}