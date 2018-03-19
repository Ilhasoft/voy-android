package br.com.ilhasoft.voy.db.report

import android.net.Uri
import br.com.ilhasoft.voy.VoyApplication
import br.com.ilhasoft.voy.db.theme.BoundDbModel
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.shared.extensions.parse
import br.com.ilhasoft.voy.shared.helpers.FileHelper
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
    var medias: RealmList<ReportFileDbModel> = RealmList()
    var newFiles: RealmList<String> = RealmList()
    var filesToDelete: RealmList<ReportFileDbModel> = RealmList()
    var createdOn: String = ""
    var shouldSend: Boolean = true
    var thumbnail: String? = null
    var lastNotification: String = ""
}

fun ReportDbModel.toReport(): Report {
    val files = mutableListOf<ReportFile>()
    medias.forEach {
        var mimeType = FileHelper.getMimeTypeFromUri(VoyApplication.instance, Uri.parse(it.file))
        mimeType = if (FileHelper.imageTypes.contains(mimeType)) {
            "image"
        } else {
            "video"
        }
        files.add(ReportFile(id = it.id, file = it.file, reportId = id, mediaType = mimeType))
    }

    return Report(
        id,
        themeId,
        Location("Point", arrayListOf(location!!.lng, location!!.lat)),
        name = name,
        createdOn = createdOn.parse("dd/MM/yyyy HH:mm"),
        description = description,
        tags = tags.toMutableList(),
        urls = urls.toMutableList(),
        status = status,
        files = files,
        thumbnail = thumbnail ?: "",
        internalId = internalId,
        shouldSend = shouldSend,
        lastNotification = lastNotification
    )
}