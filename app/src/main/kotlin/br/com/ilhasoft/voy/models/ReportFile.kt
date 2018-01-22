package br.com.ilhasoft.voy.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by developer on 05/01/18.
 */
@Parcelize
data class ReportFile(@SerializedName("title") val title: String,
                      @SerializedName("description") val description: String,
                      @SerializedName("media_type") val mediaType: String,
                      @SerializedName("file") val file: String,
                      @SerializedName("created_by") val createdBy: User,
                      @SerializedName("report_id") val reportId: Int) : Parcelable {

    companion object {
        val TAG = "reportFile"
        val TYPE_IMAGE = "image"
        val TYPE_VIDEO = "video"
    }
}