package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.base.BaseView

interface ReportContract : BaseView {

    fun fillReportsAdapter(reports: List<Report>)

    fun checkGreetings()

    fun navigateToReportDetail(report: Report)

}