package br.com.ilhasoft.voy.network.notification

import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Flowable

/**
 * Created by lucasbarros on 02/02/18.
 */
class NotificationService : ServiceFactory<NotificationApi>(NotificationApi::class.java) {

    fun getNotifications(): Flowable<List<Notification>> = api.getNotifications()
}