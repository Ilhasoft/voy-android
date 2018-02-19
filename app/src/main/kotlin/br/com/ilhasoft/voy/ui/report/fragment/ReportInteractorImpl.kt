package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.db.report.ReportDbHelper
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import io.reactivex.Flowable
import io.reactivex.Observable
import java.io.File

/**
 * Created by lucasbarros on 09/02/18.
 */
class ReportInteractorImpl(private val status: Int) : ReportInteractor {

    private val reportService by lazy { ReportService() }
    private val reportDbHelper by lazy { ReportDbHelper() }

    override fun getReports(page: Int?, pageSize: Int?, theme: Int?, mapper: Int?, status: Int?): Flowable<List<Report>> {
        return if (ConnectivityManager.isConnected()) {
            if (this@ReportInteractorImpl.status == ReportFragment.PENDING_STATUS) {
                Flowable.merge(getFromServer(page, pageSize, theme, mapper, status), getFromDb())
            } else {
                getFromServer(page, pageSize, theme, mapper, status)
            }
        } else {
            if (this@ReportInteractorImpl.status == ReportFragment.PENDING_STATUS) {
                getFromDb()
            } else {
                Flowable.empty()
            }
        }
    }

    private fun getFromDb() = reportDbHelper.getReports().onMainThread()

    private fun getFromServer(page: Int?, pageSize: Int?, theme: Int?, mapper: Int?, status: Int?): Flowable<List<Report>> {
        return reportService.getReports(page = page, page_size = pageSize, theme = theme, mapper = mapper, status = status)
                .fromIoToMainThread()
                .map { it.results }.toFlowable()
    }

    override fun resendReport(report: Report): Observable<Report> {
        val files = report.files.map { File(it.file) }
        return reportService.saveReport(report.theme, report.location!!, report.description,
                report.name, report.tags, report.urls, files)
                .doOnComplete { reportDbHelper.removeReport(report.internalId!!) }
                .map { it }
    }
}