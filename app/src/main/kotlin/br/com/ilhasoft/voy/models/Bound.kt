package br.com.ilhasoft.voy.models

import com.google.gson.annotations.SerializedName

/**
 * Created by developer on 05/01/18.
 */
data class Bound(@SerializedName("type") val type: String,
                 @SerializedName("coordinates") val coordinates: ArrayList<ArrayList<Double>>)