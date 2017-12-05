package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Notification

class HomePresenter : Presenter<HomeContract>(HomeContract::class.java) {

    fun onClickMyAccount() {
        view.navigateToMyAccount()
    }

    fun onClickNotifications() {
        view.showNotifications()
    }

    fun onClickHeaderNavView(){
        view.dismissNotifications()
    }

    fun onClickItemNotification(notification: Notification){
        view.navigateToNotificationDetail()
    }
}