package br.com.ilhasoft.voy.ui.report

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.loadControl
import br.com.ilhasoft.voy.shared.helpers.ErrorHandlerHelper
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import retrofit2.HttpException

/**
 * Created by developer on 11/01/18.
 */
class ReportsPresenter(
    private val preferences: Preferences,
    private val reportRepository: ReportRepository,
    private val scheduler: BaseScheduler,
    private val viewModel: ReportViewModel
) : Presenter<ReportsContract>(ReportsContract::class.java) {


    private var page_size = 50

    fun loadReports(theme: Int, reportStatus: Int?, page: Int?) {
        reportRepository.getReports(theme = theme, mapper = preferences.getInt(User.ID), page= page, page_size = page_size, status = reportStatus)
            .flatMap { (next, reports) ->
                    viewModel.notifyOnDemand(next)
                reportRepository.saveReports(reports)
            }
            .fromIoToMainThread(scheduler)
            .loadControl(view)
            .doOnSuccess { notifyReportsOnViewModel(it.filter { it.status == ReportStatus.APPROVED.value }, ReportStatus.APPROVED) }
            .doOnSuccess { notifyReportsOnViewModel(it.filter { it.status == ReportStatus.PENDING.value }, ReportStatus.PENDING) }
            .doOnError{ }
            .subscribe(
                { notifyReportsOnViewModel(it.filter { it.status == ReportStatus.UNAPPROVED.value }, ReportStatus.UNAPPROVED) },
                {
                    if(it is HttpException && it.code() == 404) {
                        return@subscribe
                    }
                    ErrorHandlerHelper.showError(it, R.string.report_list_error) { msg ->
                        view.showMessage(msg)
                    }
                }
            )
    }

    private fun notifyReportsOnViewModel(reports: List<Report>, status: ReportStatus) {
        viewModel.notifyReports(reports, status)
    }

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickAddReport() {
        view.navigateToAddReport()
    }

}