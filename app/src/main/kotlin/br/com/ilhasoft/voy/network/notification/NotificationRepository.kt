package br.com.ilhasoft.voy.network.notification

import br.com.ilhasoft.voy.models.Notification
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by erickjones on 02/03/18.
 */
class NotificationRepository(
        private val remoteNotificationDataSource: NotificationDataSource): NotificationDataSource {

    override fun getNotifications(): Flowable<List<Notification>> =
            remoteNotificationDataSource.getNotifications()

    override fun markAsRead(notificationId: Int): Completable =
            remoteNotificationDataSource.markAsRead(notificationId)

}