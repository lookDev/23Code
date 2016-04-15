package com.boyou.autoservice.util.sysutil

import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.view.WindowManager
import java.io.File

/**
 * Created by Mrper on 15-12-31.
 * Apk相关辅助类
 */
object ApkUtil {

    /**  设置沉浸式状态栏  **/
    @TargetApi(19)
    @JvmStatic fun setTranslucentStatusBar(activity: Activity, on: Boolean): Unit {
        val win = activity.window
        val winParams = win.attributes
        val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        winParams.flags = if (on) winParams.flags or bits else winParams.flags and bits.inv()
        win.attributes = winParams
    }

    /**
     * 根据资源名称及类型获取资源的id
     * @param context 上下文对象
     * @param srcName 资源名称
     * @param defType 资源定义的类型
     */
    @JvmStatic fun getResIdByName(context: Context, srcName: String, defType: String): Int
            = context.resources.getIdentifier(srcName, defType, context.packageName)

    /**
     * 通过id获得图片资源
     * @param context 上下文对象
     * @param drawableId 图形资源id
     */
    @JvmStatic fun getDrawableById(context: Context, drawableId: Int): Drawable {
        val drawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) context.getDrawable(drawableId) else context.resources.getDrawable(drawableId)
        drawable.bounds = Rect(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        return drawable
    }

    @JvmStatic fun getColorById(context: Context,colorId: Int): Int
        = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) context.getColor(colorId) else context.resources.getColor(colorId)

    /**
     * 获取版本值
     * @param context 上下文对象
     */
    @JvmStatic fun getVersionCode(context: Context): Int = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionCode
    } catch(e: Exception) {
        e.printStackTrace()
        0
    }

    /**
     * 获取版本名称
     * @param context 上下文对象
     */
    @JvmStatic fun getVersionName(context: Context): String = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch(e: Exception) {
        e.printStackTrace()
        "获取失败"
    }

    /**
     * 执行Apk应用程序安装
     * @param context 上下文对象
     * @param filePath 文件路径
     */
    @JvmStatic fun installApk(context: Context, filePath: String): Unit {
        var intent: Intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.fromFile(File(filePath)), "application/vnd.android.package-archive")
        context.startActivity(intent)
    }

    /**
     * 执行Apk应用程序卸载
     * @param context 上下文对象
     * @param packageName 应用包名
     */
    @JvmStatic fun uninstallApk(context: Context, packageName: String): Unit
            = context.startActivity(Intent(Intent.ACTION_DELETE, Uri.parse("package:$packageName")))


    /** 获取进程名称  */
    @JvmStatic fun getProcessName(cxt: Context, pid: Int): String? {
        val am = cxt.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses ?: return null
        for (procInfo in runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName
            }
        }
        return null
    }

}