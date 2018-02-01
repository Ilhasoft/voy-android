package br.com.ilhasoft.voy.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by developer on 14/12/17.
 */
@Parcelize
data class User(val id: Int,
           @SerializedName("first_name") val firstName: String,
           @SerializedName("last_name") val lastName: String,
           val language: String,
           val avatar: String,
           val username: String,
           val email: String,
           @SerializedName("is_mapper") val isMapper: Boolean,
           @SerializedName("is_admin") val isAdmin: Boolean,
           var password: String) : Parcelable {

    companion object {
        @JvmStatic
        val TOKEN = "token"
        @JvmStatic
        val ID = "id"
    }
}