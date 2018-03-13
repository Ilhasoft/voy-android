package br.com.ilhasoft.voy.models

import com.google.gson.annotations.SerializedName

/**
 * Created by geral on 05/12/17.
 */
data class Notification(
    var id: Int,
    var status: Int,
    var origin: Int,
    var read: Boolean,
    var message: String?,
    var report: Report,
    @SerializedName("modified_on") var modifiedOn: String
)