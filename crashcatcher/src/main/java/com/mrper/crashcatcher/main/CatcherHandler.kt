package com.mrper.crashcatcher.main

import android.content.Context
import android.os.Build
import com.j256.ormlite.dao.Dao
import com.mrper.crashcatcher.model.ExceptionInfoEntry
import com.mrper.crashcatcher.util.database.SQLiteHelper
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by admin on 2016/4/25.
 */
class CatcherHandler : Thread.UncaughtExceptionHandler {

    private var context: Context? = null
    private var catcher: Thread.UncaughtExceptionHandler? = null
    private var dao: Dao<ExceptionInfoEntry, Int>? = null

    fun init(context: Context?){
        this.context = context
        catcher = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        dao = SQLiteHelper.getInstance(this.context)?.getDao(ExceptionInfoEntry::class.java)
    }

    override fun uncaughtException(thread: Thread?, ex: Throwable?) {
        try{
            val exceptionInfo = getExceptionEntry(context,ex!!)
            dao?.create(exceptionInfo)//创建异常
        }catch(e: Exception){
            e.printStackTrace()
        }finally {
            catcher?.uncaughtException(thread, ex)
        }
    }

    /**  获取错误信息 **/
    private fun getExceptionEntry(context: Context?, ex: Throwable): ExceptionInfoEntry? {
        var execptionsWriter = StringWriter()
        var printerWriter = PrintWriter(execptionsWriter)
        var cause: Throwable? = ex.cause
        while(cause!=null){
            cause.printStackTrace(printerWriter)
            cause = cause.cause
        }
        with(ExceptionInfoEntry()) {
            appName = context?.packageManager?.getApplicationLabel(context.applicationInfo).toString()
            appPackageName = context?.packageName?:""
            appVersion = context?.packageManager?.getPackageInfo(context.packageName, 0)?.versionName ?: ""
            appIsDeal = false
            appExceptionCreateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
            appException = "${ex.message}\n\n${execptionsWriter.toString()}"
            phoneBrand = Build.BRAND
            phoneAndridVersion = Build.FINGERPRINT
            phoneModel = Build.MODEL
            return this@with
        }

    }

}