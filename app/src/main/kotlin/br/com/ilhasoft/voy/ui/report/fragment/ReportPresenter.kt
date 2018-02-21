package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.shared.extensions.loadControl
import timber.log.Timber

class ReportPresenter(
    private val preferences: Preferences,
    private val reportInteractor: ReportInteractor
) :
    Presenter<ReportContract>(ReportContract::class.java) {

    override fun attachView(view: ReportContract?) {
        super.attachView(view)
        loadReportsData()
    }

    private fun loadReportsData() {
        reportInteractor.getReports(
            page = 1, pageSize = 50,
            theme = ThemeData.themeId, mapper = preferences.getInt(User.ID), status = getStatus()
        )
            .loadControl(view)
            .subscribe({ fillReportsAdapter(it) }, { Timber.e(it) })
    }

    private fun getStatus(): Int? = view?.getStatus()

    private fun fillReportsAdapter(reports: List<Report>) {
        view?.fillReportsAdapter(reports)
    }

    fun navigateToReportDetail(report: Report) {
        view.navigateToReportDetail(report)
    }

}