package br.com.ilhasoft.voy.ui.report.detail

import br.com.ilhasoft.support.core.mvp.Presenter

class ReportDetailPresenter : Presenter<ReportDetailContract>(ReportDetailContract::class.java) {

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickPopupMenu() {
        view.showPopupMenu()
    }

    fun onClickCommentOnReport() {
        view.navigateToCommentReport()
    }

}