package br.com.ilhasoft.voy.ui.report.detail

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.Indicator

interface ReportDetailContract : BasicView {

    fun navigateBack()

    fun showPopupMenu()

    fun navigateToCommentReport()

    fun swapPage(indicator: Indicator)

}