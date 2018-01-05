package br.com.ilhasoft.voy.models

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by geral on 18/12/17.
 */
data class Media(val uri: Uri? = Uri.EMPTY, val type: String = "", val url: String = "") : Parcelable {

    constructor(source: Parcel) : this(source.readParcelable<Uri>(ClassLoader.getSystemClassLoader()),
            source.readString(),
            source.readString())

    companion object {

        val TAG = "Media"
        val TYPE_IMAGE = "image"
        val TYPE_VIDEO = "video"

        @JvmField
        val CREATOR: Parcelable.Creator<Media> = object : Parcelable.Creator<Media> {
            override fun createFromParcel(source: Parcel): Media =
                    Media(source)

            override fun newArray(size: Int): Array<Media> =
                    Array(size, { _ -> Media() })
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeParcelable(uri, flags)
            writeString(type)
            writeString(url)
        }
    }

    override fun describeContents(): Int = 0
}