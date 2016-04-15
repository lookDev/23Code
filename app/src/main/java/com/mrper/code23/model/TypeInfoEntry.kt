package com.mrper.code23.model

/**
 * Created by admin on 2016/4/15.
 */
data class TypeInfoEntry(
        @JvmField var typeName: String = "",
        @JvmField var typeValue: String = ""
){
    override fun toString(): String = typeName
}