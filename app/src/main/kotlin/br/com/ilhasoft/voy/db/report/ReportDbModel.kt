package br.com.ilhasoft.voy.db.report

import br.com.ilhasoft.voy.db.theme.BoundDbModel
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.ui.report.ReportStatus
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by lucasbarros on 09/02/18.
 */
open class ReportDbModel : RealmObject() {
    @PrimaryKey
    internal var internalId: String = UUID.randomUUID().toString()
    var id: Int = 0
    var themeId: Int = 0
    var status: Int = ReportStatus.PENDING.value
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
    /*mediasPath.forEach {
        var mimeType = FileHelper.getMimeTypeFromUri(VoyApplication.instance, Uri.parse(it))
        mimeType = if (FileHelper.imageTypes.contains(mimeType)) {
            "image"
        } else {
            "video"
        }
        files.add(ReportFile(file = it.toFilePath(), reportId = id, mediaType = mimeType))
    }
    if (mediasPath.size > 0) {
        lastImage = files.last { it.mediaType == ReportFile.TYPE_IMAGE }
    }*/

    return Report(
        id,
        themeId,
        Location("Point", arrayListOf(location!!.lng, location!!.lat)),
        name = name,
        description = description,
        tags = tags.toMutableList(),
        urls = urls.toMutableList(),
        status = status,
        files = files,
        lastImage = lastImage,
        internalId = internalId
    )
}