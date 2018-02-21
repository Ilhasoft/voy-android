package br.com.ilhasoft.voy.ui.addreport

import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

/**
 * Created by lucasbarros on 09/02/18.
 */
interface AddReportInteractor {
    fun saveReport(
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        medias: List<File>,
        urls: List<String>?
    ): Observable<Report>

    fun updateReport(
        report: Report,
        newFiles: List<File>?,
        filesToDelete: List<ReportFile>?
    ): Single<Report>

    fun updateReport(
        reportId: Int,
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        medias: List<String>,
        newFiles: List<File>?,
        filesToDelete: List<ReportFile>?
    ): Observable<Report>

    fun getTags(themeId: Int): Flowable<MutableList<String>>
}