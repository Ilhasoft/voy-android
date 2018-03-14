package br.com.ilhasoft.voy.ui.report

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.shared.extensions.extractNumbers
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.loadControl
import br.com.ilhasoft.voy.shared.helpers.ErrorHandlerHelper

/**
 * Created by developer on 11/01/18.
 */
class ReportsPresenter(
    private val preferences: Preferences,
    private val reportRepository: ReportRepository):
    Presenter<ReportsContract>(ReportsContract::class.java) {

    var viewModel: ReportViewModel? = null
    var page = 1
    var page_size = 10

    fun loadReports(theme: Int, mapper: Int) {
        reportRepository.getReports(theme = theme, mapper = mapper, page = page, page_size = page_size)
            .fromIoToMainThread()
            .loadControl(view)
            .subscribe(
                { notifyReportsOnViewModel(it) },
                {
                    it.printStackTrace()
                    ErrorHandlerHelper.showError(it, R.string.report_list_error) { msg ->
                        view.showMessage(msg)
                    }
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

    fun navigateToReportDetails(report: Report) {
        view.navigateToReportDetail(report)
    }

    fun getAvatarPositionFromPreferences(): Int =
        preferences.getString(User.AVATAR).extractNumbers().toInt().minus(1)

}