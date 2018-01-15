package br.com.ilhasoft.voy.ui.report.detail

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Indicator
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.reports.ReportsService
import br.com.ilhasoft.voy.shared.helpers.RxHelper

class ReportDetailPresenter(private val preferences: Preferences) :
        Presenter<ReportDetailContract>(ReportDetailContract::class.java) {

    private val reportService: ReportsService by lazy { ReportsService() }
    var indicator = Indicator(Uri.EMPTY, true)

    override fun attachView(view: ReportDetailContract?) {
        super.attachView(view)
        view?.showLoading()
        loadReportData()
        view?.dismissLoading()
    }

    private fun loadReportData() {
        reportService.getReport(id = getReportId(), theme = getThemeId(),
                mapper = preferences.getInt(User.ID), status = getReportStatus())
                .compose(RxHelper.defaultSingleSchedulers())
                .subscribe({ showReportData(it) }, {})
    }

    private fun getReportId(): Int = view.getReportId()

    private fun getThemeId(): Int? = view.getThemeId()

    private fun getReportStatus(): Int? = view.getReportStatus()

    fun getThemeColor(): String? = view.getThemeColor()

    private fun showReportData(report: Report) {
        view.showReportData(report)
    }

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickReportAlert() {
        view.showReportAlert()
    }

    fun onClickPopupMenu() {
        view.showPopupMenu()
    }

    fun onClickCommentOnReport() {
        view.navigateToCommentReport()
    }

    /*fun swapPage(indicator: Indicator) {
        view.swapPage(indicator)
    }

    fun getIndicators(): Collection<Indicator> = mutableListOf()*//*report.mediaList.mapIndexed { index, it ->
        Indicator(it.uri, false, index)
    }*/

}