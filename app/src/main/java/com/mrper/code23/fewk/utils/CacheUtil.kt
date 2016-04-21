package com.mrper.code23.fewk.utils

import java.io.File

/**
 * Created by Mrper on 16-1-13.
 * 缓存清理辅助类
 */
class CacheUtil private constructor(){

    companion object {

        /** 获取缓存清理实例对象  **/
        @JvmStatic fun getInstance(vararg cacheDirs: String?): CacheUtil = CacheUtil(*cacheDirs)

        /** 获取缓存清理实例对象  **/
        @JvmStatic fun getInstance(vararg cacheDirs: File): CacheUtil = CacheUtil(*cacheDirs)

        /** 获取缓存清理实例对象  **/
        @JvmStatic fun getInstance() = CacheUtil()

    }

    /** 缓存文件夹列表 **/
    lateinit var cacheDirs: MutableList<String?>

    init{
        this.cacheDirs = arrayListOf()
        this.cacheDirs.add("/data/data/com.mrper.code23/cache/") //添加应用系统内默认缓存目录
    }

    private constructor(vararg cacheDirs: String?): this(){
        if(cacheDirs.size > 0) this.cacheDirs.addAll(cacheDirs)
    }

    private constructor(vararg cacheDirs: File): this(){
        cacheDirs.forEach { this.cacheDirs.add(it.absolutePath) }
    }


    /** 获取缓存大小 **/
    fun getCacheSize(): Long {
        var length: Long = 0L
        cacheDirs.forEach { length += FileUtil.getFLength(it!!) }
        return length
    }

    /** 清理缓存文件 **/
    fun clearCache(func: (String)->Unit): Unit = cacheDirs.forEach { FileUtil.deleteF(it!!,func) }

}