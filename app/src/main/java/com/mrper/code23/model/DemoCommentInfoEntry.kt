package com.mrper.code23.model

/**
 * Created by admin on 2016/4/19.
 * 案例留言列表
 */
data class DemoCommentInfoEntry(
       @JvmField var cid: Long = 0L,
       @JvmField var rid: Long = 0L,
       @JvmField var pid: Long = 0L,
       @JvmField var rn: Long = 0L,
       @JvmField var prnum: Long = 0L,
       @JvmField var uname: String = "",
       @JvmField var uface: String = "",
       @JvmField var profileurl: String = "",
       @JvmField var cnt: String = "",
       @JvmField var time: String = "",
       @JvmField var status: Int = 0
)