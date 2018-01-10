package br.com.ilhasoft.voy.models

import com.google.gson.annotations.SerializedName

/**
 * Created by developer on 05/01/18.
 */

data class ReportFile(@SerializedName("title") val title: String,
                      @SerializedName("description") val description: String,
                      @SerializedName("media_type") val mediaType: String,
                      @SerializedName("file") val file: String,
                      @SerializedName("created_by") val createdBy: String,
                      @SerializedName("report_id") val reportId: String)