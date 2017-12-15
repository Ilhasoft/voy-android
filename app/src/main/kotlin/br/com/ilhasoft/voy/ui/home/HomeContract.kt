package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.Map

interface HomeContract : BasicView {

    fun navigateToMyAccount()

    fun selectMap()

    fun showNotifications()

    fun dismissNotifications()

    fun swapMap(map: Map?)
    
    fun navigateToNotificationDetail()

}