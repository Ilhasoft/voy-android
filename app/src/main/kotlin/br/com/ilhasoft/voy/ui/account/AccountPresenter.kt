package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.support.core.mvp.Presenter

class AccountPresenter : Presenter<AccountContract>(AccountContract::class.java) {

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickSaveMyAccount() {
        view.navigateToHome()
    }

    fun onClickSwitchAvatar() {
        view.navigateToSwitchAvatar()
    }

    fun onClickLogout() {
        view.navigateToMakeLogout()
    }

}