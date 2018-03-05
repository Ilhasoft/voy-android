package br.com.ilhasoft.voy.ui.report

import android.util.Log
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.loadControl

/**
 * Created by developer on 11/01/18.
 */
class ReportsPresenter(private val reportRepository: ReportRepository) :
    Presenter<ReportsContract>(ReportsContract::class.java) {

    var viewModel: ReportViewModel? = null

    fun loadReports() {
        reportRepository.getReports()
            .fromIoToMainThread()
            .loadControl(view)
            .subscribe(
                { notifyReportsOnViewModel(it) },
                {
                    Log.i("XXXXXXXXXX2", "ZXXXXXXXXXXXX $it")
                }
            )
    }

    private fun notifyReportsOnViewModel(reports: List<Report>) {
        viewModel?.notifyReports(reports.filter { it.status == ReportStatus.APPROVED.value }, ReportStatus.APPROVED)
        viewModel?.notifyReports(reports.filter { it.status == ReportStatus.PENDING.value }, ReportStatus.PENDING)
        viewModel?.notifyReports(reports.filter { it.status == ReportStatus.UNAPPROVED.value }, ReportStatus.UNAPPROVED)
    }

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickAddReport() {
        view.navigateToAddReport()
    }
}