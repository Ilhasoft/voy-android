package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.base.BaseView

interface ReportContract : BaseView {

    fun navigateToReportDetail(report: Report)
    fun disableLoadOnDemand()
    fun setupReportsAdapter(reports: List<Report>)
}