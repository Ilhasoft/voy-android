package br.com.ilhasoft.voy.ui.report

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.Report

interface ReportsContract : BasicView {

    fun navigateToAddReport()

    fun navigateToReportDetail(report: Report)

}