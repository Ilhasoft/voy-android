package br.com.ilhasoft.voy.ui.addreport

import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.db.report.ReportDbHelper
import br.com.ilhasoft.voy.db.theme.ThemeDbHelper
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.network.files.FilesService
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Created by lucasbarros on 09/02/18.
 */
class AddReportInteractorImpl : AddReportInteractor {

    private val reportService by lazy { ReportService() }
    private val reportDbHelper by lazy { ReportDbHelper() }

    private val themeDbHelper by lazy { ThemeDbHelper() }

    private val fileService by lazy { FilesService() }

    override fun saveReport(theme: Int, location: Location, description: String?, name: String,
                            tags: List<String>, medias: List<File>, urls: List<String>?): Observable<Report> {
        var reportInternalId: Int? = null
        return reportDbHelper.saveReport(theme, location, description, name, tags, medias.map { it.absolutePath }, urls)
                .onMainThread()
                .observeOn(Schedulers.io())
                .map {
                    reportInternalId = it.second
                    it.first
                }
                .flatMapObservable {
                    if (ConnectivityManager.isConnected()) {
                        sendReport(theme, location, description, name, tags, urls, medias, reportInternalId!!)
                    } else {
                        Observable.just(it)
                    }
                }
                .observeOn(Schedulers.io())
    }

    private fun sendReport(theme: Int, location: Location, description: String?, name: String,
                           tags: List<String>, urls: List<String>?, medias: List<File>,
                           reportInternalId: Int): Observable<Report> {
        var reportId = 0
        var report = Report()
        return reportService.saveReport(theme, location, description, name, tags, urls)
                .flatMapObservable {
                    report = it
                    reportId = it.id
                    Observable.fromIterable(medias)
                }
                .flatMapSingle { saveFile(it, reportId) }
                .doOnNext {
                    report.files.add(it)
                    if (it.mediaType == "image")
                        report.lastImage = it
                }
                .doOnComplete { reportDbHelper.removeReport(reportInternalId) }
                .map { report }
    }

    private fun saveFile(file: File, reportId: Int): Single<ReportFile> {
        return fileService.saveFile(file.nameWithoutExtension, file.name, file, "", reportId)
                .fromIoToMainThread()
    }

    override fun getTags(themeId: Int): Flowable<MutableList<String>> {
        return themeDbHelper.getThemeTags(themeId).onMainThread()
    }
}