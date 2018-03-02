package br.com.ilhasoft.voy.network.notification

import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by lucasbarros on 02/02/18.
 */
class NotificationService : ServiceFactory<NotificationApi>(NotificationApi::class.java), NotificationDataSource {

    override fun getNotifications(): Flowable<List<Notification>> = api.getNotifications()

    override fun markAsRead(notificationId: Int): Completable = api.markAsRead(notificationId)
}