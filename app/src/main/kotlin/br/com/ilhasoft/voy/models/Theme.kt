package br.com.ilhasoft.voy.models

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by geral on 13/12/17.
 */
data class Theme(
    @SerializedName("id") val id: Int,
    @SerializedName("project") val project: String = "",
    @SerializedName("bounds") val bounds: List<List<Double>> = arrayListOf(),
    @SerializedName("name") val name: String,
    @SerializedName("tags") val tags: List<String> = arrayListOf(),
    @SerializedName("color") val color: String,
    @SerializedName("allow_links") val allowLinks: Boolean,
    @SerializedName("start_at") val startAt: Date? = null,
    @SerializedName("end_at") val endAt: Date? = null
)