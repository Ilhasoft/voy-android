package br.com.ilhasoft.voy.ui.report


import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.loadControl
import br.com.ilhasoft.voy.shared.helpers.ErrorHandlerHelper
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import java.util.*
/**
 * Created by developer on 11/01/18.
 */
class ReportsPresenter(
    private val preferences: Preferences,
    private val reportRepository: ReportRepository,
    private val scheduler: BaseScheduler,
    private val viewModel: ReportViewModel
) : Presenter<ReportsContract>(ReportsContract::class.java) {

    private val PAGE_SIZE = 50

    fun loadReports(theme: Int, reportStatus: Int?, page: Int?, pageSize: Int = PAGE_SIZE) {
        reportRepository.getReports(theme = theme, mapper = preferences.getInt(User.ID), status = reportStatus, page = page, page_size = pageSize)
            .fromIoToMainThread(scheduler)
            .loadControl(view)
            .doOnSuccess { (next, _) -> viewModel.notifyOnDemand(reportStatus ?: 0, next) }
            .flatMap { (_, reports) -> reportRepository.saveReports(reports) }
            .doOnSuccess { notifyReportsOnViewModel(it.filter { it.status == ReportStatus.APPROVED.value }, ReportStatus.APPROVED) }
            .doOnSuccess { notifyReportsOnViewModel(it.filter { it.status == ReportStatus.PENDING.value }, ReportStatus.PENDING) }
            .subscribe(
                { notifyReportsOnViewModel(it.filter { it.status == ReportStatus.UNAPPROVED.value }, ReportStatus.UNAPPROVED) },
                {
                    ErrorHandlerHelper.showError(it, R.string.report_list_error) { msg ->
                        view.showMessage(msg)
                    }
                }
            )
    }

    // TODO remove method body duplication
    fun loadByPage(page: String, status: Int) {
        reportRepository.getReports(page)
            .fromIoToMainThread(scheduler)
            .loadControl(view)
            .doOnSuccess { (next, _) -> viewModel.notifyOnDemand(status, next) }
            .flatMap { (_, reports) -> reportRepository.saveReports(reports) }
            .doOnSuccess { notifyReportsOnViewModel(it.filter { it.status == ReportStatus.APPROVED.value }, ReportStatus.APPROVED) }
            .doOnSuccess { notifyReportsOnViewModel(it.filter { it.status == ReportStatus.PENDING.value }, ReportStatus.PENDING) }
            .subscribe(
                { notifyReportsOnViewModel(it.filter { it.status == ReportStatus.UNAPPROVED.value }, ReportStatus.UNAPPROVED) },
                {
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

    fun onClickAddReport(currentTime: Date) {
        if ((ThemeData.startAt..ThemeData.endAt).contains(currentTime))
            view.navigateToAddReport()
        else
            view.showMessage(R.string.period_ended_text)
    }

}