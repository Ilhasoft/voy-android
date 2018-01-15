package br.com.ilhasoft.voy.ui.report.detail

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.Report

interface ReportDetailContract : BasicView {

    fun getReportId(): Int

    fun getThemeId(): Int?

    fun getReportStatus(): Int?

    fun getThemeColor(): String?

    fun showReportData(report: Report)

    fun navigateBack()

    fun showReportAlert()

    fun showPopupMenu()

    fun navigateToCommentReport()

    /*fun swapPage(indicator: Indicator)*/

}