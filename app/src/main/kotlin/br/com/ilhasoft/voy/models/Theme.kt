package br.com.ilhasoft.voy.models

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by geral on 13/12/17.
 */
data class Theme(@SerializedName("id") val id: Int,
                 @SerializedName("project") val project: String = "",
                 @SerializedName("bounds") val bounds: List<List<Double>> = arrayListOf(),
                 @SerializedName("name") val name: String,
//                 @SerializedName("description") val description: String = "",
                 @SerializedName("tags") val tags: List<String> = arrayListOf(),
                 @SerializedName("color") val color: String,
//                 @SerializedName("pin") val pin: String,
//                 @SerializedName("reports_count") val reportsCount: Int,
//                 @SerializedName("created_on") val createdOn: String = "",
                 @SerializedName("allow_links") val allowLinks: Boolean,
                 @SerializedName("start_at") val startAt: Date = Date(),
                 @SerializedName("end_at") val endAt: Date = Date())