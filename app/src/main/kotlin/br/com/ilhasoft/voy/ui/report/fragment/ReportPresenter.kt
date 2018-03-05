package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.shared.extensions.extractNumbers

class ReportPresenter(
    private val preferences: Preferences,
    private val reportInteractor: ReportInteractor
) : Presenter<ReportContract>(ReportContract::class.java) {

    /*fun loadReportsData(status: Int) {
        reportInteractor.getReports(
            page = 1, pageSize = 50,
            theme = ThemeData.themeId, mapper = preferences.getInt(User.ID), status = status
        )
            .loadControl(view)
            .doOnTerminate { view?.checkGreetings() }
            .subscribe({ fillReportsAdapter(it) }, { Timber.e(it) })
    }

    private fun fillReportsAdapter(reports: List<Report>) {
        view?.fillReportsAdapter(reports)
    }*/

    fun navigateToReportDetail(report: Report) {
        view.navigateToReportDetail(report)
    }

    fun getAvatarPositionFromPreferences(): Int =
            preferences.getString(User.AVATAR).extractNumbers().toInt().minus(1)

}