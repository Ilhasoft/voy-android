package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.core.mvp.Presenter

class HomePresenter : Presenter<HomeContract>(HomeContract::class.java) {

    fun onClickAddReport() {
        view.navigateToAddReport()
    }

}