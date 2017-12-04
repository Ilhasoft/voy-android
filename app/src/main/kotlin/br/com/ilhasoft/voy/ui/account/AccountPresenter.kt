package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.support.core.mvp.Presenter

class AccountPresenter : Presenter<AccountContract>(AccountContract::class.java) {

    fun onClickSwitchAvatar() {
        view.navigateToSwitchAvatar()
    }

    fun onClickLogout() {
        view.navigateToMakeLogout()
    }

}