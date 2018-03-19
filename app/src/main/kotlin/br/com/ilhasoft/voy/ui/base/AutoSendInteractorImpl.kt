package br.com.ilhasoft.voy.ui.base

import br.com.ilhasoft.voy.db.report.ReportDbHelper
import br.com.ilhasoft.voy.db.report.ReportDbModel
import br.com.ilhasoft.voy.db.report.toReport
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import br.com.ilhasoft.voy.shared.schedulers.StandardScheduler
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import timber.log.Timber
import java.io.File

/**
 * Created by lucasbarros on 20/02/18.
 */
class AutoSendInteractorImpl : AutoSendInteractor {

    companion object {
        private var sendingPendingReports = false
    }

    private val reportService by lazy { ReportService() }
    private val reportDbHelper by lazy { ReportDbHelper(Realm.getDefaultInstance(), StandardScheduler()) }

    private fun getFromDb() = reportDbHelper.getReportDbModels().onMainThread()

    override fun sendPendingReports() {
        if (!sendingPendingReports) {
            getFromDb()
                .doOnSubscribe { sendingPendingReports = true }
                .flatMap {
                    Flowable.fromIterable(it)
                }
                .filter { it.shouldSend }
                .flatMap {
                    resendReport(it).fromIoToMainThread()
                        //TODO: See other possibilities for backpressure strategy
                        .toFlowable(BackpressureStrategy.DROP)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { reportDbHelper.removeReport(it.internalId) }
                }
                .doAfterTerminate { sendingPendingReports = false }
                .subscribe({}, {
                    it.printStackTrace()
                    Timber.e(it)
                })
        }
    }

    private fun resendReport(reportDbModel: ReportDbModel): Observable<Report> {
        val report = reportDbModel.toReport()
        return if (report.id == 0) {
            reportService.saveReport(
                report.theme,
                report.location!!,
                report.description,
                report.name,
                report.tags,
                report.urls,
                reportDbModel.medias.map { File(it.file) },
                report.thumbnail
            )
        } else {
            reportService.updateReport(
                report.id,
                report.theme,
                report.location!!,
                report.description,
                report.name,
                report.tags,
                report.urls,
                reportDbModel.newFiles.map { File(it) },
                reportDbModel.filesToDelete.map { it.id },
                report.thumbnail
            )
        }
    }
}