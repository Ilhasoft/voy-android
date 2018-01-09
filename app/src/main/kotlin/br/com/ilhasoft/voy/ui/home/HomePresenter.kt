package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Map
import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.network.reports.ReportsService
import br.com.ilhasoft.voy.shared.rx.RxHelper
import timber.log.Timber

class HomePresenter : Presenter<HomeContract>(HomeContract::class.java) {

    private var selectedMap: Map? = null
    val srv = ReportsService()

    fun onClickMyAccount() {
        view.navigateToMyAccount()
    }

    fun onClickSelectMap() {
        view.selectMap()
    }

    fun onClickNotifications() {
        srv.getReport(1)
                .compose(RxHelper.defaultSingleSchedulers())
                .subscribe({
                    val i = it.createdOn
                } , {
                    Timber.e(it)
                })
//        view.showNotifications()
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