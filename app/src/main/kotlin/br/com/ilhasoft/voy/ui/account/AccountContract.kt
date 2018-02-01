package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.ui.base.BaseView

interface AccountContract : BaseView {

    fun navigateBack()

    fun navigateToHome()

    fun navigateToSwitchAvatar()

    fun swapAvatar()

    fun navigateToMakeLogout()

    fun setUser(user: User)

    fun saveUser()

    fun isValidUser(): Boolean

    fun userUpdatedMessage()

}