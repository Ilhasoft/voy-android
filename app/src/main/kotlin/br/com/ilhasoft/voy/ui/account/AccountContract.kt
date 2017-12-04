package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.support.core.mvp.BasicView

interface AccountContract : BasicView {

    fun navigateToSwitchAvatar()

    fun navigateToMakeLogout()

}