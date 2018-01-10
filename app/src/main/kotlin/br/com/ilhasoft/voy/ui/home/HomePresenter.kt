package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Map
import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.network.comments.CommentsService
import br.com.ilhasoft.voy.shared.rx.RxHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.sql.Time

class HomePresenter : Presenter<HomeContract>(HomeContract::class.java) {

    private var selectedMap: Map? = null
    private val srv = CommentsService()

    fun onClickMyAccount() {
        view.navigateToMyAccount()
    }

    fun onClickSelectMap() {
        view.selectMap()
    }

    fun onClickNotifications() {

        srv.deleteComment(1686)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { Timber.e("SUCCESS1") }
                .subscribe({
                    Timber.e("SUCCESS")
                }, {
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