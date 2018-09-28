package br.com.ilhasoft.voy.ui.home

import android.accounts.NetworkErrorException
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.extensions.emitFlowableError
import br.com.ilhasoft.voy.models.*
import br.com.ilhasoft.voy.shared.schedulers.ImmediateScheduler
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * Created by Allan Lima on 05/03/2018.
 */
class HomePresenterTest {

    private lateinit var presenter: HomePresenter
    @Mock
    private lateinit var view: HomeContract
    @Mock
    private lateinit var interactor: HomeInteractor

    @Mock
    private lateinit var preferences: Preferences

    private val mockedUserId = 1
    private val mockedProject = Project(1, "", "", "", "", null, "", "", arrayListOf(), arrayListOf())
    private val mockedProjectList = mutableListOf(
            mockedProject.copy(id = 1),
            mockedProject.copy(id = 2),
            mockedProject.copy(id = 3)
    )

    private val mockedTheme = Theme(1, Project(), listOf(), "", listOf(), "", false)
    private val mockedThemeList = mutableListOf(
            mockedTheme.copy(id = 1),
            mockedTheme.copy(id = 2),
            mockedTheme.copy(id = 3)
    )

    private val mockedNotification = Notification(1, 1, 1, false, "", mock(Report::class.java), "")
    private val mockedNotificationList = mutableListOf(
            mockedNotification.copy(id = 1),
            mockedNotification.copy(id = 2),
            mockedNotification.copy(id = 3)
    )
    private val mockedLanguage: String = "en"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(preferences.getInt(User.ID)).thenReturn(mockedUserId)

        presenter = HomePresenter(preferences, interactor, ImmediateScheduler(), mockedLanguage)
        presenter.attachView(view)
    }

    @After
    fun tearDown() {
        presenter.detachView()
    }

    @Test
    fun shouldLoadProjectsOnStart() {
        `when`(interactor.getProjects(mockedUserId, mockedLanguage)).thenReturn(Flowable.just(mockedProjectList))
        `when`(interactor.getThemes(mockedProject.id, mockedUserId, mockedLanguage)).thenReturn(Flowable.just(mockedThemeList))

        presenter.start()

        verify(view).showLoading()
        verify(view).fillProjectsAdapter(mockedProjectList)
        verify(view).fillThemesAdapter(mockedThemeList)
        verify(view).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenTimeoutOnLoadProjects() {
        `when`(interactor.getProjects(mockedUserId, mockedLanguage)).thenReturn(emitFlowableError(TimeoutException()))

        presenter.start()

        verify(view).showLoading()
        verify(view).showMessage(R.string.http_request_error)
        verify(view).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenUnknownHostOnLoadProjects() {
        `when`(interactor.getProjects(mockedUserId, mockedLanguage)).thenReturn(emitFlowableError(UnknownHostException()))

        presenter.start()

        verify(view).showLoading()
        verify(view).showMessage(R.string.login_network_error)
        verify(view).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenNetworkErrorOnLoadProjects() {
        `when`(interactor.getProjects(mockedUserId, mockedLanguage)).thenReturn(emitFlowableError(NetworkErrorException()))

        presenter.start()

        verify(view).showLoading()
        verify(view).showMessage(R.string.login_network_error)
        verify(view).dismissLoading()
    }

    @Test
    fun shouldLoadNotificationsWhenOnResume() {
        `when`(interactor.getNotifications()).thenReturn(Flowable.just(mockedNotificationList))

        presenter.resume()

        verify(view).fillNotificationAdapter(mockedNotificationList)
    }

    @Test
    fun shouldNavigateToUserAccountWhenOnClickMyAccount() {
        presenter.onClickMyAccount()

        verify(view).navigateToMyAccount()
    }

    @Test
    fun shouldSelectProjectWhenOnClickChooseProject() {
        presenter.onClickChooseProject()

        verify(view).selectProject()
    }

    @Test
    fun shouldShowNotificationsWhenOnClickNotifications() {
        presenter.onClickNotifications()

        verify(view).showNotifications()
    }

    @Test
    fun shouldCloseNotificationsWhenOnClickHeaderNavView() {
        presenter.onClickHeaderNavView()

        verify(view).dismissNotifications()
    }

    @Test
    fun shouldLoadThemesDataWhenOnClickProject() {
        `when`(interactor.getThemes(mockedProject.id, mockedUserId, mockedLanguage)).thenReturn(Flowable.just(mockedThemeList))

        presenter.onClickProject(mockedProject)

        verify(view).swapProject(mockedProject)
        verify(view).showLoading()
        verify(view).fillThemesAdapter(mockedThemeList)
        verify(view).dismissLoading()
    }

    @Test
    fun shouldNavigateToNotificationDetailsWhenOnClickItemNotification() {
        val mockedTheme = mock(Theme::class.java)
        `when`(interactor.markAsRead(mockedNotification.id)).thenReturn(Completable.complete())
        `when`(interactor.getTheme(mockedNotification.report.theme, mockedLanguage)).thenReturn(Maybe.just(mockedTheme))

        presenter.onClickItemNotification(mockedNotification)

        verify(view).showLoading()
        verify(view).putThemeOnThemeData(mockedTheme)
        verify(view).navigateToNotificationDetail(mockedNotification)
        verify(view).dismissLoading()
    }

    @Test
    fun shouldNavigateToThemeReportsWhenOnClickTheme() {
        presenter.onClickTheme(mockedTheme)

        verify(view).navigateToThemeReports(mockedTheme)
    }

}