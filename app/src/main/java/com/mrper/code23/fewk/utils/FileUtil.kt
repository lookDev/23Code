package com.mrper.code23.fewk.utils

import org.jetbrains.annotations.NotNull
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Created by Mrper on 16-1-2.
 * 文件辅助方法
 */

object FileUtil {

    /**
     * 文件保存,如果文件已经存在则删除该文件并新建
     * @param filename 文件名
     * @param data 要写入的数据
     */
    @JvmStatic fun saveFile(filename: String?, data: InputStream?): Boolean = try {
        val file = File(filename)
        if (file.exists()) file.delete() else file.parentFile.mkdirs() //如果文件存在就删除，不存在就创建所有其父目录
        val fos = FileOutputStream(file)
        var buffer: ByteArray? = ByteArray(2048)
        var length: Int = data!!.read(buffer, 0, buffer!!.count())
        while (length != -1) {
            fos.write(buffer, 0, length)
            buffer = ByteArray(2048)
            length = data.read(buffer, 0, buffer.count())
        }
        data.close()
        fos.flush()
        fos.close()
        true
    } catch(e: Exception) {
        e.printStackTrace()
        false
    }

    /**
     * 文件保存,如果文件已经存在则删除该文件并新建
     * @param filename 文件名
     * @param data 要写入的数据
     */
    @JvmStatic fun saveFile(filename: String?, data: InputStream?,funReport: (Long)->Unit): Boolean = try {
        val file = File(filename)
        if (file.exists()) file.delete() else file.parentFile.mkdirs() //如果文件存在就删除，不存在就创建所有其父目录
        val fos = FileOutputStream(file)
        var buffer: ByteArray? = ByteArray(2048)
        var length: Int = data!!.read(buffer, 0, buffer!!.count())
        var writeLength = 0L
        while (length != -1) {
            fos.write(buffer, 0, length)
            writeLength += length
            funReport(writeLength)
            buffer = ByteArray(2048)
            length = data.read(buffer, 0, buffer.count())
        }
        data.close()
        fos.flush()
        fos.close()
        true
    } catch(e: Exception) {
        e.printStackTrace()
        false
    }

    /**
     * 储存数据到文件中
     * @param filename 文件名
     * @param data 数据
     */
    @JvmStatic fun saveFile(filename: String?,data: ByteArray): Boolean = try{
        val file = File(filename)
        if (file.exists()) file.delete() else file.parentFile.mkdirs() //如果文件存在就删除，不存在就创建所有其父目录
        val fos = FileOutputStream(file)
        fos.write(data,0,data.size)
        fos.close()
        true
    }catch(e: Exception){
        e.printStackTrace()
        false
    }

    /**
     * 获取文件或文件夹的大小
     * @param fname 文件或文件夹名
     */
    @JvmStatic fun getFLength(fname: String): Long = with(File(fname)) {
        when {
            isFile -> length()
            isDirectory -> {
                var length: Long = 0L
                listFiles().forEach { length += getFLength(it.absolutePath) }
                length
            }
            else -> 0L
        }
    }

    /**
     * 获取文件或文件夹的大小
     * @param f 文件或文件夹file对象
     */
    @JvmStatic fun getFLength(f: File): Long = getFLength(f.absolutePath)

    /**
     * 删除文件或清理文件夹内的文件，不处理删除文件夹
     * @param fname 文件或文件夹名
     * @param func 回调函数，用于显示正在删除的文件名
     */
    @JvmStatic
    @JvmOverloads
    fun deleteF(fname: String, func: ((String) -> Unit)? = null): Unit = with(File(fname)) {
        if (isFile) {
            val name = absolutePath
            if (delete()) func?.invoke(name) //如果删除成功，回调出文件名
        } else if (isDirectory) {
            listFiles().forEach { deleteF(it.absolutePath, func) }
        }
    }

    /**
     * 删除文件或清理文件夹内的文件，不处理删除文件夹
     * @param f 文件或文件夹的file对象
     * @param func 回调函数，用于显示正在删除的文件名
     */
    @JvmStatic
    @JvmOverloads
    fun deleteF(f: File, func: ((String) -> Unit)? = null): Unit = deleteF(f.absoluteFile, func)

    /**
     * 获取剩余空间内存
     * @param sdcard
     */
    fun getSpaceMemory(@NotNull sdcard: File): Long = sdcard.freeSpace

    /**
     * 获取剩余空间内存
     * @param sdcardPath
     */
    fun getSpaceMemory(@NotNull sdcardPath: String) = getSpaceMemory(File(sdcardPath))

}