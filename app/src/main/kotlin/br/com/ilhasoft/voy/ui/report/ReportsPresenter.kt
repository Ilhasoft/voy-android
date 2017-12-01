package br.com.ilhasoft.voy.ui.report

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Report

class ReportsPresenter : Presenter<ReportsContract>(ReportsContract::class.java) {

    fun onClickAddReport() {
        view.navigateToAddReport()
    }

    fun navigateToReportDetail(report: Report) {
        view.navigateToReportDetail(report)
    }

}