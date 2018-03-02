package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.notification.NotificationDataSource
import br.com.ilhasoft.voy.network.notification.NotificationRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeoutException

/**
 * Created by erickjones on 28/02/18.
 */
class NotificationServiceTest {

    @Mock
    private lateinit var notificationService: NotificationDataSource

    private lateinit var notificationRepository: NotificationRepository

    private val mockNotificationId = 34

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        notificationRepository = NotificationRepository(notificationService)
    }

    @Test
    fun shouldReturnNotificationsList() {
        `when`(notificationService.getNotifications())
                .thenReturn(Flowable.just(createMockNotificationList()))

        notificationRepository.getNotifications()
                .test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors()
    }

    @Test
    fun shouldNotReturnNotificationsList() {
        `when`(notificationService.getNotifications())
                .thenReturn(Flowable.error(TimeoutException()))

        notificationRepository.getNotifications()
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

    @Test
    fun shouldMarkNotificationAsRead() {
        `when`(notificationService.markAsRead(mockNotificationId))
                .thenReturn(Completable.complete())

        notificationRepository.markAsRead(mockNotificationId)
                .test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors()
    }

    @Test
    fun shouldNotMarkNotificationAsRead() {
        `when`(notificationService.markAsRead(mockNotificationId))
                .thenReturn(Completable.error(TimeoutException()))

        notificationRepository.markAsRead(mockNotificationId)
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

    private fun createMockNotificationList(): List<Notification> {
        return listOf(
                Notification(1, 1, 1, false, null, Report(), ""),
                Notification(2, 3, 3, false, null, Report(), ""),
                Notification(3, 3, 3, false, null, Report(), ""),
                Notification(4, 4, 4, false, null, Report(), "")
        )
    }

}