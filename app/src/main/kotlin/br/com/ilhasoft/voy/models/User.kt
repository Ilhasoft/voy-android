package br.com.ilhasoft.voy.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by developer on 14/12/17.
 */
data class User(val id: Int,
                @SerializedName("first_name") val firstName: String,
                @SerializedName("last_name") val lastName: String,
                val language: String,
                val avatar: String,
                val username: String,
                val email: String,
                @SerializedName("is_mapper") val isMapper: Boolean,
                @SerializedName("is_admin") val isAdmin: Boolean,
                var password: String?) : Parcelable {

    companion object {
        @JvmStatic
        val TOKEN = "token"

        @JvmStatic
        val ID = "id"

        @JvmStatic
        val USERNAME = "username"

        @JvmStatic
        val AVATAR = "avatar"

        @JvmStatic
        val EMAIL = "email"

        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }

    }

    constructor(id: Int, avatar: String, username: String, email: String) : this(
            id,
            "",
            "",
            "",
            avatar,
            username,
            email,
            false,
            false,
            null
    )

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            1 == source.readInt(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(firstName)
        writeString(lastName)
        writeString(language)
        writeString(avatar)
        writeString(username)
        writeString(email)
        writeInt((if (isMapper) 1 else 0))
        writeInt((if (isAdmin) 1 else 0))
        writeString(password)
    }
}