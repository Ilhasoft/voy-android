package br.com.ilhasoft.voy.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by developer on 01/12/17.
 */

@Parcelize
data class Report(@SerializedName("id") var id: Int = 0,
                  @SerializedName("theme") var theme: Int = 0,
                  @SerializedName("location") var location: Location? = null,
                  @SerializedName("can_receive_comments") var canReceiveComments: Boolean = false,
                  @SerializedName("editable") var editable: Boolean = false,
                  @SerializedName("visible") var visible: Boolean = false,
                  @SerializedName("created_on") var createdOn: Date = Date(),
                  @SerializedName("description") var description: String? = "",
                  @SerializedName("name") var name: String = "",
                  @SerializedName("tags") var tags: ArrayList<String> = arrayListOf(),
                  @SerializedName("theme_color") var themeColor: String = "",
                  @SerializedName("pin") var pin: String = "",
                  @SerializedName("created_by") var createdBy: User? = null,
                  @SerializedName("last_image") var lastImage: ReportFile? = null,
                  @SerializedName("status") var status: Int = 0,
                  @SerializedName("urls") var urls: ArrayList<String> = arrayListOf(),
                  @SerializedName("files") var files: ArrayList<ReportFile> = arrayListOf(),
                  @SerializedName("last_notification") var lastNotification: String? = ""
) : Parcelable