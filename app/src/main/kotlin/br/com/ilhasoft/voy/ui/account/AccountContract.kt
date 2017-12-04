package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.support.core.mvp.BasicView

interface AccountContract : BasicView {

    fun navigateBack()

    fun navigateToHome()

    fun navigateToSwitchAvatar()

    fun navigateToMakeLogout()

}