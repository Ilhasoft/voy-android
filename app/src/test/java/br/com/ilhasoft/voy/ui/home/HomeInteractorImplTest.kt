package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.network.notification.NotificationDataSource
import br.com.ilhasoft.voy.network.notification.NotificationRepository
import br.com.ilhasoft.voy.network.projects.ProjectDataSource
import br.com.ilhasoft.voy.network.projects.ProjectRepository
import br.com.ilhasoft.voy.network.themes.ThemeDataSource
import br.com.ilhasoft.voy.network.themes.ThemeRepository
import br.com.ilhasoft.voy.shared.schedulers.ImmediateScheduler
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

/**
 * Created by felipe on 12/03/18.
 */
class HomeInteractorImplTest {

    private lateinit var homeInteractor: HomeInteractorImpl
    private lateinit var themeRepository: ThemeRepository
    private lateinit var projectRepository: ProjectRepository
    private lateinit var notificationRepository: NotificationRepository
    @Mock
    private lateinit var themeRemoteDataSource: ThemeDataSource
    @Mock
    private lateinit var themeLocalDataSource: ThemeDataSource
    @Mock
    private lateinit var projectDataSource: ProjectDataSource
    @Mock
    private lateinit var notificationDataSource: NotificationDataSource
    @Mock
    private lateinit var connectionProvider: CheckConnectionProvider

    private val mockedNotifications = listOf(mock(Notification::class.java))
    private val mockedThemes = mutableListOf(mock(Theme::class.java), mock(Theme::class.java))
    private val mockedNotificationId = 1
    private val mockedProjectId = 1
    private val mockedUserId = 1

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        themeRepository = ThemeRepository(themeRemoteDataSource, themeLocalDataSource, connectionProvider)
        projectRepository = ProjectRepository(projectDataSource)
        notificationRepository = NotificationRepository(notificationDataSource)
        homeInteractor = HomeInteractorImpl(
            themeRepository, projectRepository,
            notificationRepository, ImmediateScheduler()
        )
    }

    @Test
    fun `Should get themes and save on cache when online`() {
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(themeRemoteDataSource.getThemes(mockedProjectId, mockedUserId)).thenReturn(Flowable.just(mockedThemes))
        `when`(themeLocalDataSource.saveThemes(mockedThemes)).thenReturn(Flowable.just(mockedThemes))

        homeInteractor.getThemes(mockedProjectId, mockedUserId)
            .test()
            .assertComplete()
            .assertValues(mockedThemes)
    }

    @Test
    fun `Should get a list of notification when success`() {
        `when`(notificationDataSource.getNotifications()).thenReturn(
            Flowable.just(
                mockedNotifications
            )
        )

        homeInteractor.getNotifications()
            .test()
            .assertComplete()
            .assertValue(mockedNotifications)
    }

    @Test
    fun `Should throw error when get a list of notification`() {
        `when`(notificationDataSource.getNotifications()).thenReturn(Flowable.error(Exception()))

        homeInteractor.getNotifications()
            .test()
            .assertError(Exception::class.java)
    }

    @Test
    fun `Should mark notification read when valid notification id`() {
        `when`(notificationDataSource.markAsRead(mockedNotificationId)).thenReturn(Completable.complete())

        homeInteractor.markAsRead(mockedNotificationId)
            .test()
            .assertComplete()
    }
}