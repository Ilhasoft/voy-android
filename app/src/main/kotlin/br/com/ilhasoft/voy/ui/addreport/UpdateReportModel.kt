package br.com.ilhasoft.voy.ui.addreport

import br.com.ilhasoft.voy.models.Location
import java.io.File

/**
 * Created by lucasbarros on 22/02/18.
 */
data class UpdateReportModel(
    var reportId: Int,
    var location: Location,
    var description: String?,
    var name: String,
    var tags: List<String>,
    var urls: List<String>?,
    var newFiles: List<File>?,
    var reportInternalId: Int
)