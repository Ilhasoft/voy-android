package br.com.ilhasoft.voy.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by developer on 07/12/17.
 */
data class Tag(var name: String = "", var selected: Boolean = false) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString())

    constructor() : this("")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tag> {
        override fun createFromParcel(parcel: Parcel): Tag {
            return Tag(parcel)
        }

        override fun newArray(size: Int): Array<Tag?> {
            return arrayOfNulls(size)
        }
    }
}