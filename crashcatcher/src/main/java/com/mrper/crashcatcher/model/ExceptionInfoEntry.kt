package com.mrper.crashcatcher.model

import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable


/**
 * Created by admin on 2016/4/25.
 */
@DatabaseTable(tableName = "tbl_ex")
data class ExceptionInfoEntry(
        @JvmField @DatabaseField(generatedId = true,canBeNull = false) var id: Int = 0,
        @JvmField @DatabaseField var appName: String = "",
        @JvmField @DatabaseField var appVersion: String = "",
        @JvmField @DatabaseField var appPackageName: String = "",
        @JvmField @DatabaseField var appException: String = "",
        @JvmField @DatabaseField var appExceptionCreateTime: String = "",
        @JvmField @DatabaseField var appIsDeal: Boolean = false,
        @JvmField @DatabaseField var phoneBrand: String = "",
        @JvmField @DatabaseField var phoneModel: String = "",
        @JvmField @DatabaseField var phoneAndridVersion: String = ""
) : Parcelable {

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) = with(dest!!){
        writeInt(id)
        writeString(appName)
        writeString(appVersion)
        writeString(appPackageName)
        writeString(appException)
        writeString(appExceptionCreateTime)
        writeValue(appIsDeal)
        writeString(phoneBrand)
        writeString(phoneModel)
        writeString(phoneAndridVersion)
    }

    companion object {

        @JvmField val CREATOR = object: android.os.Parcelable.Creator<ExceptionInfoEntry>{

            override fun createFromParcel(source: Parcel?): ExceptionInfoEntry? =
                    ExceptionInfoEntry(
                            source!!.readInt(),
                            source.readString(),
                            source.readString(),
                            source.readString(),
                            source.readString(),
                            source.readString(),
                            source.readValue(Boolean::class.java.classLoader) as Boolean,
                            source.readString(),
                            source.readString(),
                            source.readString()
                    )

            override fun newArray(size: Int): Array<out ExceptionInfoEntry>? =
                    Array(size,{ ExceptionInfoEntry() })

        }

    }

}