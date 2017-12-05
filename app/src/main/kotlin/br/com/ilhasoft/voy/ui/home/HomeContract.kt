package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.core.mvp.BasicView

interface HomeContract : BasicView {

    fun navigateToMyAccount()

    fun selectMap()

    fun showNotifications()

}