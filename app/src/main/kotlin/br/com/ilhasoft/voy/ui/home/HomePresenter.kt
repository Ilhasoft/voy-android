package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Map
import br.com.ilhasoft.voy.models.Notification

class HomePresenter : Presenter<HomeContract>(HomeContract::class.java) {

    private var selectedMap: Map? = null

    fun onClickMyAccount() {
        view.navigateToMyAccount()
    }

    fun onClickSelectMap() {
        view.selectMap()
    }

    fun onClickNotifications() {
        view.showNotifications()
    }

    fun onClickHeaderNavView() {
        view.dismissNotifications()
    }

    fun onClickMap(map: Map?) {
        view.swapMap(map)
    }

    fun onClickItemNotification(notification: Notification) {
        view.navigateToNotificationDetail()
    }

    fun setSelectedMap(map: Map?) {
        selectedMap = map
    }

    fun getSelectedMap(): Map? = selectedMap

}