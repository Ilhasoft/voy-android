package br.com.ilhasoft.voy.models

import com.google.gson.annotations.SerializedName

/**
 * Created by developer on 05/01/18.
 */
data class Project(@SerializedName("id") var id: Int = 0,
                   @SerializedName("name") var name: String = "",
                   @SerializedName("description") var description: String = "",
                   @SerializedName("path") var path: String = "",
                   @SerializedName("language") var language: String = "",
                   @SerializedName("bounds") var bounds: Bound? = null,
                   @SerializedName("thumbnail") var thumbnail: String = "",
                   @SerializedName("window_title") var windowTitle: String = "",
                   @SerializedName("languages") var languages: ArrayList<ArrayList<String>>? = null,
                   @SerializedName("years") var years: ArrayList<String>? = null)