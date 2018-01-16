package br.com.ilhasoft.voy.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by developer on 05/01/18.
 */
@Parcelize
data class ReportMedia(@SerializedName("urls") val urls: ArrayList<String>,
                       @SerializedName("files") val files: ArrayList<ReportFile>) : Parcelable