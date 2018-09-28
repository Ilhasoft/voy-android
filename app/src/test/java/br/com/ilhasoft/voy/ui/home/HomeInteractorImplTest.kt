package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.models.Project
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
    private lateinit var projectRemoteDataSource: ProjectDataSource
    @Mock
    private lateinit var projectLocalDataSource: ProjectDataSource
    @Mock
    private lateinit var notificationDataSource: NotificationDataSource
    @Mock
    private lateinit var connectionProvider: CheckConnectionProvider

    private val mockedNotifications = listOf(mock(Notification::class.java))
    private val mockedThemes = mutableListOf(mock(Theme::class.java), mock(Theme::class.java))
    private val mockedProjects = mutableListOf(mock(Project::class.java), mock(Project::class.java))
    private val mockedNotificationId = 1
    private val mockedProjectId = 1
    private val mockedUserId = 1
    private val mockedLanguage = "en"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        themeRepository = ThemeRepository(themeRemoteDataSource, themeLocalDataSource, connectionProvider)
        projectRepository = ProjectRepository(projectRemoteDataSource, projectLocalDataSource, connectionProvider)
        notificationRepository = NotificationRepository(notificationDataSource)
        homeInteractor = HomeInteractorImpl(
            themeRepository, projectRepository,
            notificationRepository, ImmediateScheduler()
        )
    }

    @Test
    fun `Should get projects and save on cache when online`() {
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(projectRemoteDataSource.getProjects(mockedLanguage)).thenReturn(Flowable.just(mockedProjects))
        `when`(projectLocalDataSource.saveProjects(mockedProjects)).thenReturn(Flowable.just(mockedProjects))

        homeInteractor.getProjects(mockedUserId, mockedLanguage)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValues(mockedProjects)
    }

    @Test
    fun `Should get projects when offline`() {
        `when`(connectionProvider.hasConnection()).thenReturn(false)
        `when`(projectLocalDataSource.getProjects(mockedLanguage)).thenReturn(Flowable.just(mockedProjects))

        homeInteractor.getProjects(mockedUserId, mockedLanguage)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValues(mockedProjects)
    }

    @Test
    fun `Should get themes and save on cache when online`() {
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(themeRemoteDataSource.getThemes(mockedProjectId, mockedUserId, mockedLanguage)).thenReturn(Flowable.just(mockedThemes))
        `when`(themeLocalDataSource.saveThemes(mockedThemes)).thenReturn(Flowable.just(mockedThemes))

        homeInteractor.getThemes(mockedProjectId, mockedUserId, mockedLanguage)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValues(mockedThemes)
    }

    @Test
    fun `Should get themes when offline`() {
        `when`(connectionProvider.hasConnection()).thenReturn(false)
        `when`(themeLocalDataSource.getThemes(mockedProjectId, mockedUserId, mockedLanguage)).thenReturn(Flowable.just(mockedThemes))

        homeInteractor.getThemes(mockedProjectId, mockedUserId, mockedLanguage)
            .test()
            .assertNoErrors()
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
            .assertNoErrors()
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
            .assertNoErrors()
    }
}