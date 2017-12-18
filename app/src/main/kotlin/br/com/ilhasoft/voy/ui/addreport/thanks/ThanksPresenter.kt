package br.com.ilhasoft.voy.ui.addreport.thanks

import br.com.ilhasoft.support.core.mvp.Presenter

class ThanksPresenter : Presenter<ThanksContract>(ThanksContract::class.java) {

    fun navigateToAddReport() {
        view.navigateToAddReport()
    }

    fun onClickClose() {
        view.onClickClose()
    }
}