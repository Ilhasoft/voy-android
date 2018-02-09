package br.com.ilhasoft.voy.db.report

import br.com.ilhasoft.voy.db.theme.BoundDbModel
import br.com.ilhasoft.voy.db.theme.StringDbModel
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by lucasbarros on 09/02/18.
 */
open class ReportDbModel : RealmObject() {
    var id: Int = 0
    var themeId: Int = 0
    var location: BoundDbModel = BoundDbModel()
    var name: String = "ReportName"
    var description: String? = null
    var tags: RealmList<StringDbModel> = RealmList()
    var urls: RealmList<StringDbModel> = RealmList()
    var mediasPath: RealmList<StringDbModel> = RealmList()
    var sent: Boolean = false
    var createdOn: String = ""
    var lastNotification: String = ""
    var lastImage: String = ""
}

fun ReportDbModel.toReport(): Report {
    return Report(id,
            themeId,
            Location("Point", arrayListOf(location.lng, location.lat)),
            name = name,
            description = description,
            tags = tags.map { it.text }.toMutableList(),
            urls = urls.map { it.text }.toMutableList())
}