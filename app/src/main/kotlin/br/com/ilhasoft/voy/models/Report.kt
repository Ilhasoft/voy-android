package br.com.ilhasoft.voy.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by developer on 01/12/17.
 */

@Parcelize
class Report(@SerializedName("id") var id: Int = 0,
             @SerializedName("theme") var theme: Int = 0,
             @SerializedName("location") var location: Location? = null,
             @SerializedName("can_receive_comments") var canReceiveComments: Boolean = false,
             @SerializedName("editable") var editable: Boolean = false,
             @SerializedName("visible") var visible: Boolean = false,
             @SerializedName("created_on") var createdOn: String = "",
             @SerializedName("description") var description: String = "",
             @SerializedName("name") var name: String = "",
             @SerializedName("tags") var tags: ArrayList<String>? = null,
             @SerializedName("theme_color") var themeColor: String = "",
             @SerializedName("pin") var pin: String = "",
             @SerializedName("created_by") var createdBy: User? = null,
             @SerializedName("last_image") var lastImage: LastImage? = null,
             @SerializedName("status") var status: Int = 0) : Parcelable {

    companion object {
        @JvmStatic
        val TAG = "Report"
    }

}