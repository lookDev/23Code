package com.mrper.crashcatcher.util.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.mrper.crashcatcher.model.ExceptionInfoEntry

/**
 * Created by admin on 2016/4/25.
 */
class SQLiteHelper(context: Context?,dbName: String,version: Int) : OrmLiteSqliteOpenHelper(context,dbName,null,version) {

    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        try{
            TableUtils.createTableIfNotExists(connectionSource,ExceptionInfoEntry::class.java)
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    override fun onUpgrade(database: SQLiteDatabase?, connectionSource: ConnectionSource?, oldVersion: Int, newVersion: Int) {
    }

    companion object {

        private var sqlHelper: SQLiteHelper? = null

        @JvmStatic fun getInstance(context: Context?): SQLiteHelper?{
            if(sqlHelper == null){
                synchronized(SQLiteHelper::class.java){
                    sqlHelper = SQLiteHelper(context,"ExDb.db",1)
                }
            }
            return sqlHelper
        }

    }

}