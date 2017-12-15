package br.com.ilhasoft.voy.ui.report.detail

import br.com.ilhasoft.support.core.mvp.BasicView

interface ReportDetailContract : BasicView {

    fun navigateBack()

    fun showPopupMenu()

    fun navigateToCommentReport()

}