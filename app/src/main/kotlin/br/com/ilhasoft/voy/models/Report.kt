package br.com.ilhasoft.voy.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by developer on 01/12/17.
 */
data class Report(@SerializedName("id") var id: Int = 0,
                  @SerializedName("theme") var theme: Int = 0,
                  @SerializedName("location") var location: Location? = null,
                  @SerializedName("can_receive_comments") var canReceiveComments: Boolean = false,
                  @SerializedName("editable") var editable: Boolean = false,
                  @SerializedName("visible") var visible: Boolean = false,
                  @SerializedName("created_on") var createdOn: Date = Date(),
                  @SerializedName("description") var description: String? = "",
                  @SerializedName("name") var name: String = "",
                  @SerializedName("tags") var tags: MutableList<String> = mutableListOf(),
                  @SerializedName("theme_color") var themeColor: String = "",
                  @SerializedName("pin") var pin: String = "",
                  @SerializedName("created_by") var createdBy: User? = null,
                  @SerializedName("last_image") var lastImage: ReportFile? = null,
                  @SerializedName("status") var status: Int = 0,
                  @SerializedName("urls") var urls: MutableList<String> = mutableListOf(),
                  @SerializedName("files") var files: MutableList<ReportFile> = mutableListOf(),
                  @SerializedName("last_notification") var lastNotification: String? = ""
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Report> = object : Parcelable.Creator<Report> {
            override fun createFromParcel(source: Parcel): Report = Report(source)
            override fun newArray(size: Int): Array<Report?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt(),
            source.readParcelable<Location>(Location::class.java.classLoader),
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt(),
            source.readSerializable() as Date,
            source.readString(),
            source.readString(),
            source.createStringArrayList(),
            source.readString(),
            source.readString(),
            source.readParcelable<User>(User::class.java.classLoader),
            source.readParcelable<ReportFile>(ReportFile::class.java.classLoader),
            source.readInt(),
            source.createStringArrayList(),
            source.createTypedArrayList(ReportFile.CREATOR),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeInt(theme)
        writeParcelable(location, 0)
        writeInt((if (canReceiveComments) 1 else 0))
        writeInt((if (editable) 1 else 0))
        writeInt((if (visible) 1 else 0))
        writeSerializable(createdOn)
        writeString(description)
        writeString(name)
        writeStringList(tags)
        writeString(themeColor)
        writeString(pin)
        writeParcelable(createdBy, 0)
        writeParcelable(lastImage, 0)
        writeInt(status)
        writeStringList(urls)
        writeTypedList(files)
        writeString(lastNotification)
    }
}