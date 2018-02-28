package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.network.notification.NotificationApi
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

/**
 * Created by erickjones on 28/02/18.
 */
class NotificationServiceTest {

    private lateinit var notificationService: MockNotificationService
    private val mockNotificationId = 34

    @Before
    fun setup() {
        notificationService = MockNotificationService()
    }

    @Test
    fun shouldReturnNotificationsList() {
        notificationService.getNotifications()
                .test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors()
    }

    @Test
    fun shouldNotReturnNotificationsList() {
        notificationService.getNotifications()
                .test()
                .assertSubscribed()
                .assertError { it is UnknownHostException }
    }

    @Test
    fun shouldMarkNotificationAsRead() {
        notificationService.markAsRead(mockNotificationId)
                .test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors()
    }

}

class MockNotificationService() : ServiceFactory<NotificationApi>(NotificationApi::class.java) {

    fun getNotifications(): Flowable<List<Notification>> =
            Flowable.just(mutableListOf<Notification>())

    fun markAsRead(notificationId: Int): Completable =
            Completable.complete()

}