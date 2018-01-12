package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.reports.ReportsService
import br.com.ilhasoft.voy.shared.rx.RxHelper

class ReportPresenter : Presenter<ReportContract>(ReportContract::class.java) {

    private val reportService: ReportsService by lazy { ReportsService() }

    override fun attachView(view: ReportContract?) {
        super.attachView(view)
        view?.showLoading()
        loadReportsData()
        view?.dismissLoading()
    }

    private fun loadReportsData() {
        reportService.getReports(page = 1, page_size = 50, theme = getThemeId(), status = getStatus())
                .compose(RxHelper.defaultSingleSchedulers())
                .subscribe({ fillReportsAdapter(it.results) }, {})
    }

    private fun getThemeId(): Int? = view?.getThemeId()

    private fun getStatus(): Int? = view?.getStatus()

    private fun fillReportsAdapter(reports: List<Report>) {
        view?.fillReportsAdapter(reports)
    }

    fun navigateToReportDetail(report: Report) {
        view.navigateToReportDetail(report)
    }

    fun onClickAddReport() {
        view.navigateToAddReport()
    }

    fun onClickEditReport(report: Report?) {
        view?.navigateToEditReport(report)
    }

}