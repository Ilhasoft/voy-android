package br.com.ilhasoft.voy.models

import com.google.gson.annotations.SerializedName

/**
 * Created by developer on 05/01/18.
 */
data class ReportMedia(@SerializedName("urls") val urls: ArrayList<String>,
                       @SerializedName("files") val files: ArrayList<ReportFile>)