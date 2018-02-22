package br.com.ilhasoft.voy.ui.base

import br.com.ilhasoft.voy.db.report.ReportDbHelper
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
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
    private val reportDbHelper by lazy { ReportDbHelper() }

    private fun getFromDb() = reportDbHelper.getReports().onMainThread()

    override fun sendPendingReports() {
        if (false) {
//        if (!sendingPendingReports) {
            getFromDb().observeOn(Schedulers.newThread())
                .doOnSubscribe { sendingPendingReports = true }
                .doOnTerminate { sendingPendingReports = false }
                .flatMap {
                    Flowable.fromIterable(it)
                }
                .flatMap {
                    resendReport(it)
                        //TODO: See other possibilities for backpressure strategy
                        .toFlowable(BackpressureStrategy.DROP)
                }
                .subscribe({}, {
                    Timber.e(it)
                })
        }
    }

    private fun resendReport(report: Report): Observable<Report> {
        val files = report.files.map { File(it.file) }
        return reportService.saveReport(
            report.theme,
            report.location!!,
            report.description,
            report.name,
            report.tags,
            report.urls,
            files
        )
            .doOnComplete { reportDbHelper.removeReport(report.internalId!!) }
    }
}