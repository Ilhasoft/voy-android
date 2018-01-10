package br.com.ilhasoft.voy.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by geral on 18/12/17.
 */
@Parcelize
data class Media(val uri: Uri? = Uri.EMPTY, val type: String = "", val url: String = "") : Parcelable {

    companion object {
        val TAG = "Media"
        val TYPE_IMAGE = "image"
        val TYPE_VIDEO = "video"
    }

}