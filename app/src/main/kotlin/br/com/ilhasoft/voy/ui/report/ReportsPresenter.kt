package br.com.ilhasoft.voy.ui.report

import br.com.ilhasoft.support.core.mvp.Presenter

class ReportsPresenter : Presenter<ReportsContract>(ReportsContract::class.java) {

    fun onClickAddReport() {
        view.navigateToAddReport()
    }

}