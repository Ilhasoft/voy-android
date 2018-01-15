package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User

class AccountPresenter(private val preferences: Preferences) :
        Presenter<AccountContract>(AccountContract::class.java) {

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
        preferences.remove(User.TOKEN)
        preferences.remove(User.ID)
        view.navigateToMakeLogout()
    }

    fun setSelectedAvatar(drawableId: Int?) {
        avatarDrawableId = drawableId
        view.swapAvatar()
    }

    fun getSelectedAvatar() = avatarDrawableId

}