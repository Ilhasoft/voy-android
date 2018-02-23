package br.com.ilhasoft.voy.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by developer on 05/01/18.
 */
data class ReportFile(val id: Int = 0,
                      val title: String = "",
                      val description: String = "",
                      @SerializedName("media_type") val mediaType: String = "",
                      val file: String = "",
                      @SerializedName("created_by") val createdBy: User? = null,
                      @SerializedName("report_id") val reportId: Int = 0) : Parcelable {

    companion object {
        val TAG = "reportFile"

        val TYPE_IMAGE = "image"

        val TYPE_VIDEO = "video"

        @JvmField
        val CREATOR: Parcelable.Creator<ReportFile> = object : Parcelable.Creator<ReportFile> {
            override fun createFromParcel(source: Parcel): ReportFile = ReportFile(source)
            override fun newArray(size: Int): Array<ReportFile?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<User>(User::class.java.classLoader),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(title)
        writeString(description)
        writeString(mediaType)
        writeString(file)
        writeParcelable(createdBy, 0)
        writeInt(reportId)
    }
}