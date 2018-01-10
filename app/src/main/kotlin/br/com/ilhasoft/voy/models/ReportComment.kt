package br.com.ilhasoft.voy.models

import com.google.gson.annotations.SerializedName

/**
 * Created by developer on 14/12/17.
 */
data class ReportComment(@SerializedName("id") val id: Int,
                         @SerializedName("text") var text: String,
                         @SerializedName("created_by") val createdBy: User,
                         @SerializedName("created_on") val createdOn: String,
                         @SerializedName("modified_on") val modifiedOn: String,
                         @SerializedName("report") val report: Int)