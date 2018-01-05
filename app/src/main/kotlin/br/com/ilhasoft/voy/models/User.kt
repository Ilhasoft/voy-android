package br.com.ilhasoft.voy.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by developer on 14/12/17.
 */
@Parcelize
class User(@SerializedName("id") val id: Int,
           @SerializedName("first_name") val firstName: String,
           @SerializedName("last_name") val lastName: String,
           @SerializedName("language") val language: String,
           @SerializedName("avatar") val avatar: String,
           @SerializedName("username") val username: String,
           @SerializedName("is_mapper") val isMapper: Boolean,
           @SerializedName("is_admin") val isAdmin: Boolean) : Parcelable