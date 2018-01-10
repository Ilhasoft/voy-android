package br.com.ilhasoft.voy.models

import com.google.gson.annotations.SerializedName

/**
 * Created by developer on 07/12/17.
 */
data class Tag(@SerializedName("id") val id: Int, @SerializedName("tag") val tag: String)