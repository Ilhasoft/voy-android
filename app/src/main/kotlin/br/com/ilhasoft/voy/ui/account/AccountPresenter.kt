package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.support.core.mvp.Presenter

class AccountPresenter : Presenter<AccountContract>(AccountContract::class.java) {

    private var avatarDrawableId: Int? = null

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

    fun setSelectedAvatar(drawableId: Int?) {
        avatarDrawableId = drawableId
        view.swapAvatar()
    }

    fun getSelectedAvatar() = avatarDrawableId

}