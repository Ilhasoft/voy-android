package br.com.ilhasoft.voy.report.fragment

import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.reports.ReportDataSource
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.shared.schedulers.ImmediateScheduler
import br.com.ilhasoft.voy.ui.report.fragment.ReportContract
import br.com.ilhasoft.voy.ui.report.fragment.ReportPresenter
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*

/**
 * Created by developer on 13/03/18.
 */
class ReportPresenterTest {

    @Mock
    private lateinit var preferences: Preferences
    @Mock
    private lateinit var reportRemoteDataSource: ReportDataSource
    @Mock
    private lateinit var reportLocalDataSource: ReportDataSource
    @Mock
    private lateinit var connectionProvider: CheckConnectionProvider

    @Mock
    private lateinit var view: ReportContract
    private lateinit var repository: ReportRepository
    private lateinit var presenter: ReportPresenter

    private val mockedLocation = createMockedLocation()
    private val mockedUser = createMockedUser()
    private val mockedReport = createMockedReport()

    private val mockedUserAvatar = "http://voy-dev.ilhasoft.mobi/media/users/avatars/group-21.png"

    private val mockedThemeId = 1
    private val mockedStatusId = 1
    private val mockedMapperId = 1
    private val mockedPage = "1"
    private val mockedPageSize = 50
    private val mockedList = listOf(mock(Report::class.java), mock(Report::class.java))
    private val mockedPair = mockedPage to mockedList

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = ReportRepository(reportRemoteDataSource, reportLocalDataSource, connectionProvider)
        presenter = ReportPresenter(preferences, repository, ImmediateScheduler())
        presenter.attachView(view)
    }

    @Test
    fun shouldNavigateToShowReportDetail() {
        presenter.navigateToReportDetail(mockedReport)

        verify(view).navigateToReportDetail(mockedReport)
    }

    @Test
    fun shouldGetAvatarReference() {
        `when`(preferences.getString(User.AVATAR)).thenReturn(mockedUserAvatar)

        presenter.getAvatarPositionFromPreferences()

        assertEquals(preferences.getString(User.AVATAR), mockedUserAvatar)
    }

    @Test
    fun `Should get reports from service when online`() {
        `when`(preferences.getInt(User.ID)).thenReturn(mockedMapperId)
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(reportRemoteDataSource.getReports(mockedThemeId,null, mockedMapperId,
            mockedStatusId, mockedPage.toInt(), mockedPageSize))
            .thenReturn(Single.just(mockedPair))
        `when`(reportLocalDataSource.saveReports(mockedList)).thenReturn(Single.just(mockedList))

        presenter.loadReports(mockedThemeId, mockedStatusId, mockedPage.toInt())

        verify(view).showLoading()
        verify(view).dismissLoading()
        verify(view).disableLoadOnDemand(mockedPage.isNullOrBlank())
        verify(view).setupReportsAdapter(mockedList)
    }

    @Test
    fun `Should get reports from cache when offline`() {
        `when`(connectionProvider.hasConnection()).thenReturn(false)
        `when`(preferences.getInt(User.ID)).thenReturn(mockedMapperId)
        `when`(reportLocalDataSource.getReports(
            theme = mockedThemeId,
            mapper = mockedMapperId,
            status = mockedStatusId)
        ).thenReturn(Single.just(mockedPair))

        presenter.loadReports(mockedThemeId, mockedStatusId, mockedPage.toInt())

        verify(view).showLoading()
        verify(view).dismissLoading()
        verify(view).disableLoadOnDemand(mockedPage.isNullOrBlank())
        verify(view).setupReportsAdapter(mockedList)
    }

    @Test
    fun `Should show an error message when something wrong`() {
        `when`(preferences.getInt(User.ID)).thenReturn(mockedMapperId)
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(reportRemoteDataSource.getReports(mockedThemeId,null, mockedMapperId,
            mockedStatusId, mockedPage.toInt(), mockedPageSize)
        ).thenReturn(Single.error(Exception()))

        presenter.loadReports(mockedThemeId, mockedStatusId, mockedPage.toInt())

        verify(view).showLoading()
        verify(view).dismissLoading()
        verify(view).showMessage(R.string.report_list_error)
    }

    private fun createMockedLocation() = Location("GPS", arrayListOf(1.2, 2.2))

    private fun createMockedUser() = User(
            1,
            "firstName",
            "lastName",
            "language",
            "avatar",
            "username",
            "email",
            true,
            true,
            "password"
    )

    private fun createMockedReport() = Report(
            1,
            1,
            mockedLocation,
            true,
            true,
            true,
            Date(),
            "description",
            "name",
            mutableListOf(),
            "themeColor",
            mockedUser,
            "thumbnail",
            1,
            mutableListOf(),
            mutableListOf(),
            "lastNotification"
    )

}