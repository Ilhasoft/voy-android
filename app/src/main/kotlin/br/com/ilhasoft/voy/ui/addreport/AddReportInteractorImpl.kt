package br.com.ilhasoft.voy.ui.addreport

import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.db.report.ReportDbHelper
import br.com.ilhasoft.voy.db.report.ReportFileDbModel
import br.com.ilhasoft.voy.db.theme.ThemeDbHelper
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.shared.extensions.format
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import br.com.ilhasoft.voy.shared.schedulers.StandardScheduler
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.io.File
import java.util.*

/**
 * Created by lucasbarros on 09/02/18.
 */
class AddReportInteractorImpl(val reportRepository: ReportRepository) : AddReportInteractor {

    private val reportDbHelper by lazy { ReportDbHelper(Realm.getDefaultInstance(), StandardScheduler()) }

    private val themeDbHelper by lazy { ThemeDbHelper(Realm.getDefaultInstance(), StandardScheduler()) }

    override fun saveReport(
        reportInternalId: String?,
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        medias: List<File>,
        urls: List<String>?
    ): Observable<Report> {
        return reportDbHelper.saveReport(
            reportInternalId,
            theme,
            location,
            description,
            name,
            tags,
            urls,
            medias.map {
                ReportFileDbModel().apply {
                file = it.absolutePath
            }
            }.toMutableList(),
            createdOn = Date().format("dd/MM/yyyy HH:mm")
        )
            .observeOn(Schedulers.io())
            .flatMapObservable {
                if (ConnectivityManager.isConnected()) {
                    reportRepository.saveReport(theme, location, description, name, tags, urls, medias)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { reportDbHelper.removeReport(it.internalId!!) }
                } else {
                    Observable.just(it)
                }
            }
            .observeOn(Schedulers.io())
    }

    override fun updateReport(
        reportInternalId: String?,
        reportId: Int,
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        medias: MutableList<ReportFileDbModel>,
        newFiles: List<File>?,
        filesToDelete: List<ReportFile>?
    ): Observable<Report> {
        return reportDbHelper.saveReport(
            reportInternalId,
            theme,
            location,
            description,
            name,
            tags,
            urls,
            medias,
            reportId,
            newFiles?.map { it.absolutePath },
            filesToDelete,
            createdOn = Date().format("dd/MM/yyyy HH:mm")
        )
            .onMainThread()
            .observeOn(Schedulers.io())
            .flatMapObservable {
                if (ConnectivityManager.isConnected()) {
                    updateReportInternal(
                        reportId,
                        location,
                        description,
                        name,
                        tags,
                        urls,
                        newFiles,
                        filesToDelete?.map { it.id },
                        it.internalId!!
                    )
                } else {
                    Observable.just(it)
                }
            }
    }

    override fun getTags(themeId: Int): Flowable<MutableList<String>> {
        return themeDbHelper.getThemeTags(themeId).onMainThread()
    }

    private fun updateReportInternal(
        reportId: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        newFiles: List<File>?,
        filesToDelete: List<Int>?,
        reportInternalId: String
    ): Observable<Report>? {
        return reportRepository.updateReport(
            reportId,
            ThemeData.themeId,
            location,
            description,
            name,
            tags,
            urls,
            newFiles,
            filesToDelete
        )
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { reportDbHelper.removeReport(reportInternalId) }
    }
}
