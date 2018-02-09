package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.helpers.RxHelper

class ReportPresenter(private val preferences: Preferences,
                      private val reportInteractor: ReportInteractor) :
        Presenter<ReportContract>(ReportContract::class.java) {

    private val reportService: ReportService by lazy { ReportService() }

    override fun attachView(view: ReportContract?) {
        super.attachView(view)
        view?.showLoading()
        loadReportsData()
        view?.dismissLoading()
    }

    private fun loadReportsData() {
        reportInteractor.getReports(page = 1, page_size = 50,
                theme = ThemeData.themeId, mapper = preferences.getInt(User.ID), status = getStatus())
                .subscribe({ fillReportsAdapter(it) }, {})
    }

    private fun getStatus(): Int? = view?.getStatus()

    private fun fillReportsAdapter(reports: List<Report>) {
        view?.fillReportsAdapter(reports)
    }

    fun navigateToReportDetail(report: Report) {
        view.navigateToReportDetail(report)
    }

    fun onClickEditReport(report: Report?) {
        view?.navigateToEditReport(report)
    }

}