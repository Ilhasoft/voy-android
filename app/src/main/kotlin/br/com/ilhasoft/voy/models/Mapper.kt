package br.com.ilhasoft.voy.models

import com.google.gson.annotations.SerializedName

/**
 * Created by developer on 05/01/18.
 */
data class Mapper(@SerializedName("id") val id: Int,
                  @SerializedName("full_name") val fullname: String,
                  @SerializedName("username") val username: String)