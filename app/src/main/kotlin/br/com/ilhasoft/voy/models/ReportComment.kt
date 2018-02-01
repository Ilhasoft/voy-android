package br.com.ilhasoft.voy.models

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by developer on 14/12/17.
 */
data class ReportComment(
        val id: Int,
        var text: String,
        @SerializedName("created_by") val createdBy: User,
        @SerializedName("created_on") val createdOn: Date,
        @SerializedName("modified_on") val modifiedOn: Date,
        val report: Int
)