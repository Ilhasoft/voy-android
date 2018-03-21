package br.com.ilhasoft.voy.ui.report.fragment

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
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler

class ReportPresenter(
    private val preferences: Preferences,
    private val reportRepository: ReportRepository,
    private val scheduler: BaseScheduler
) : Presenter<ReportContract>(ReportContract::class.java) {

    val pageSize = 50

    fun loadReports(theme: Int, status: Int, page: Int) {
        reportRepository.getReports(
            theme = theme,
            mapper = preferences.getInt(User.ID),
            status = status,
            page = page,
            page_size = pageSize
        )
            .fromIoToMainThread(scheduler)
            .loadControl(view)
            .doOnSuccess { (next, _) -> view.disableLoadOnDemand(next.isNullOrBlank()) }
            .flatMap { (_, reports) -> reportRepository.saveReports(reports) }
            .subscribe(
                { view.setupReportsAdapter(it.filter { it.status == status }) },
                {
                    ErrorHandlerHelper.showError(it, R.string.report_list_error) { msg ->
                        view.showMessage(msg)
                    }
                }
            )
    }

    fun navigateToReportDetail(report: Report) {
        view.navigateToReportDetail(report)
    }

    fun getAvatarPositionFromPreferences(): Int =
        preferences.getString(User.AVATAR).extractNumbers().toInt().minus(1)

}