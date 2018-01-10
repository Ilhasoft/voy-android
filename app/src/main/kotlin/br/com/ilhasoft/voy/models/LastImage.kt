package br.com.ilhasoft.voy.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by developer on 03/01/18.
 */
@Parcelize
class LastImage(@SerializedName("title") var title: String,
                @SerializedName("description") var description: String,
                @SerializedName("media_type") var mediaType: String,
                @SerializedName("file") var file: String,
                @SerializedName("created_by") var createdBy: User,
                @SerializedName("report_id") var reportId: Int) : Parcelable