package br.com.ilhasoft.voy.network.notification

import br.com.ilhasoft.voy.models.Notification
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by erickjones on 02/03/18.
 */
interface NotificationDataSource {
    fun getNotifications(): Flowable<List<Notification>>
    fun markAsRead(notificationId: Int): Completable
}