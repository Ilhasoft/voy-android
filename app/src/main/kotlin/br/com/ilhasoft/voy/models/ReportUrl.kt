package br.com.ilhasoft.voy.models

import com.google.gson.annotations.SerializedName

/**
 * Created by lucasbarros on 10/01/18.
 */
data class ReportUrl(@SerializedName("report_id") var reportId: Int, var url: String)