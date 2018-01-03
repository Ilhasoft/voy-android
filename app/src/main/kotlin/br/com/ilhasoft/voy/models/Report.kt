package br.com.ilhasoft.voy.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by developer on 01/12/17.
 */
data class Report(var title: String = "",
                  var createdAt: String = "",
                  var description: String = "",
                  var theme: Theme? = null,
                  var mediaList: MutableList<Media> = mutableListOf(),
                  var externalLinks: MutableList<String> = mutableListOf(),
                  var tagsList: MutableList<Tag> = mutableListOf()) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Theme::class.java.classLoader)) {
        parcel.readTypedList(mediaList, Media.CREATOR)
        parcel.readStringList(externalLinks)
        parcel.readTypedList(tagsList, Tag.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(createdAt)
        parcel.writeString(description)
        parcel.writeParcelable(theme, flags)
        parcel.writeList(mediaList)
        parcel.writeList(externalLinks)
        parcel.writeList(tagsList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Report> {
        val TAG = "Report"
        override fun createFromParcel(parcel: Parcel): Report {
            return Report(parcel)
        }

        override fun newArray(size: Int): Array<Report?> {
            return arrayOfNulls(size)
        }
    }

}