package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.Report

interface ReportContract : BasicView {

    fun getThemeId(): Int?

    fun getStatus(): Int?

    fun fillReportsAdapter(reports: List<Report>)

    fun navigateToReportDetail(report: Report)

    fun navigateToAddReport()

    fun navigateToEditReport(report: Report?)

}