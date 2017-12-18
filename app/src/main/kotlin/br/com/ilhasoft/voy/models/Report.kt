package br.com.ilhasoft.voy.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by developer on 01/12/17.
 */
data class Report(var title: String,
                  var createdAt: String,
                  var description: String,
                  var mediaList: MutableList<Media> = mutableListOf()) : Parcelable {

    constructor() : this("", "", "", mutableListOf())
    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString()) {
        source.readTypedList(mediaList, Media.CREATOR)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Report> = object : Parcelable.Creator<Report> {
            override fun newArray(size: Int): Array<Report> =
                Array(size, { _ -> Report() })

            override fun createFromParcel(source: Parcel): Report = Report(source)
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeString(title)
            writeString(createdAt)
            writeString(description)
            writeTypedList(mediaList)
        }
    }

    override fun describeContents(): Int = 0
}