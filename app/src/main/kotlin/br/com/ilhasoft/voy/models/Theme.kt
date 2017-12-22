package br.com.ilhasoft.voy.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by geral on 13/12/17.
 */
data class Theme(val project: String = "",
                 val name: String = "",
                 val description: String = "",
                 val color: String= "",
                 val pin: String = "",
                 val tags: MutableList<Tag> = mutableListOf()) : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
        parcel.readTypedList(tags, Tag.CREATOR)
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(project)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(color)
        parcel.writeString(pin)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Theme> {
        override fun createFromParcel(parcel: Parcel): Theme {
            return Theme(parcel)
        }

        override fun newArray(size: Int): Array<Theme?> {
            return arrayOfNulls(size)
        }
    }

}