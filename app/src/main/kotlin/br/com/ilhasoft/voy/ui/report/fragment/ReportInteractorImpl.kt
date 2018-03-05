package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.db.report.ReportDbHelper
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import br.com.ilhasoft.voy.ui.report.ReportStatus
import io.reactivex.Flowable

/**
 * Created by lucasbarros on 09/02/18.
 */
class ReportInteractorImpl(private val status: Int) : ReportInteractor {

    private val reportService by lazy { ReportService() }
    private val reportDbHelper by lazy { ReportDbHelper() }

    override fun getReports(page: Int?, pageSize: Int?, theme: Int?, mapper: Int?, status: Int?): Flowable<List<Report>> {
        return if (ConnectivityManager.isConnected()) {
            if (this@ReportInteractorImpl.status == ReportStatus.PENDING.value) {
                Flowable.merge(getFromServer(page, pageSize, theme, mapper, status), getFromDb())
            } else {
                getFromServer(page, pageSize, theme, mapper, status)
            }
        } else {
            getFromDb()
        }
    }

    private fun getFromDb() = reportDbHelper.getReports().onMainThread()

    private fun getFromServer(page: Int?, pageSize: Int?, theme: Int?, mapper: Int?, status: Int?): Flowable<List<Report>> {
        return reportService.getReports(
            page = page, page_size = pageSize, theme = theme,
            mapper = mapper, status = status
        )
            .flatMapPublisher { saveOnCache(it.results) }
//            .map { it.results }
//            .toFlowable()
            .fromIoToMainThread()
    }


    // TODO save reports on cache when take it from the API
    private fun saveOnCache(reports: List<Report>): Flowable<List<Report>> {
        return Flowable.just(reports)
            .flatMap { Flowable.fromIterable(it) }
            .flatMapSingle { reportDbHelper.saveReport(it) }
            .toList()
            .toFlowable()
    }

}