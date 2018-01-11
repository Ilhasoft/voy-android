package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.reports.ReportsService
import br.com.ilhasoft.voy.shared.rx.RxHelper
import timber.log.Timber

class ReportPresenter : Presenter<ReportContract>(ReportContract::class.java) {

    private val reportService: ReportsService by lazy { ReportsService() }

    override fun attachView(view: ReportContract?) {
        super.attachView(view)
        view?.showLoading()
        loadReportsData()
        view?.dismissLoading()
    }

    private fun loadReportsData() {
        if (getTheme() == 1) {
            reportService.getReports(theme = getTheme(), status = getStatus())
                    .compose(RxHelper.defaultSingleSchedulers())
                    .subscribe({
                        fillReportsAdapter(it.results)
                        println(it)
                    }, {
                        Timber.d(it.localizedMessage)
                    })
        }
    }

    private fun getTheme(): Int? = view?.getTheme()

    private fun getStatus(): Int? = view?.getStatus()

    private fun fillReportsAdapter(reports: List<Report>) {
        view?.fillReportsAdapter(reports)
    }

    fun onClickAddReport() {
        view.navigateToAddReport()
    }

    fun navigateToReportDetail(report: Report) {
        view.navigateToReportDetail(report)
    }

}