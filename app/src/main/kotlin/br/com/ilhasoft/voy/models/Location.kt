package br.com.ilhasoft.voy.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by developer on 03/01/18.
 */

@Parcelize
data class Location(@SerializedName("type") val type: String,
               @SerializedName("coordinates") val coordinates: ArrayList<Double>) : Parcelable