package br.com.ilhasoft.voy.db.report

import br.com.ilhasoft.voy.db.theme.BoundDbModel
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.ui.report.fragment.ReportFragment
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by lucasbarros on 09/02/18.
 */
open class ReportDbModel : RealmObject() {
    @PrimaryKey
    var internalId: Int = 0
    var id: Int = 0
    var themeId: Int = 0
    var location: BoundDbModel? = BoundDbModel()
    var name: String = "ReportName"
    var description: String? = null
    var tags: RealmList<String> = RealmList()
    var urls: RealmList<String> = RealmList()
    var mediasPath: RealmList<String> = RealmList()
    var newFiles: RealmList<String> = RealmList()
    var filesToDelete: RealmList<ReportFileDbModel> = RealmList()
    var createdOn: String = ""
}

fun ReportDbModel.toReport(): Report {
    var lastImage: ReportFile? = null
    val files = mutableListOf<ReportFile>()
    mediasPath.forEach {
        files.add(ReportFile(file = it, reportId = id))
    }
    if (mediasPath.size > 0) {
        //FIXME: Verify if it is an image
        lastImage = files.last()
    }

    return Report(
        id,
        themeId,
        Location("Point", arrayListOf(location!!.lng, location!!.lat)),
        name = name,
        description = description,
        tags = tags.toMutableList(),
        urls = urls.toMutableList(),
        status = ReportFragment.PENDING_STATUS,
        files = files,
        lastImage = lastImage,
        internalId = internalId
    )
}