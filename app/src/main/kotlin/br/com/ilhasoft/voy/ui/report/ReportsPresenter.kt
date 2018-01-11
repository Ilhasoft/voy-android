package br.com.ilhasoft.voy.ui.report

import br.com.ilhasoft.support.core.mvp.Presenter

/**
 * Created by developer on 11/01/18.
 */
class ReportsPresenter : Presenter<ReportsContract>(ReportsContract::class.java) {

    fun onClickNavigateBack() {
        view?.navigateBack()
    }

}