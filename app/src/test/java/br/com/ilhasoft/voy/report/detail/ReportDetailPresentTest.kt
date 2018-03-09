package br.com.ilhasoft.voy.report.detail

import android.accounts.NetworkErrorException
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.extensions.emitSingleError
import br.com.ilhasoft.voy.models.*
import br.com.ilhasoft.voy.network.reports.ReportDataSource
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.network.reports.ReportsApi
import br.com.ilhasoft.voy.shared.schedulers.ImmediateScheduler
import br.com.ilhasoft.voy.ui.report.detail.ReportDetailContract
import br.com.ilhasoft.voy.ui.report.detail.ReportDetailPresenter
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeoutException

/** Create missing tests below for code coverage:
 *
 *  getCarouselItems
 *  getIndicators
 *  checkDetachView
 *
 *  But pay attention, because this methods depends on Android Framework.
 */
class ReportDetailPresentTest {

    @Mock
    private lateinit var connectionProvider: CheckConnectionProvider

    @Mock
    private lateinit var dataSource: ReportDataSource

    @Mock
    private lateinit var preferences: Preferences

    @Mock
    private lateinit var view: ReportDetailContract
    private lateinit var presenter: ReportDetailPresenter

    private val mockedIndicator = Indicator()
    private val mockedLocation = createMockedLocation()
    private val mockedUser = createMockedUser()
    private val mockedReport = createMockedReport()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = ReportDetailPresenter(mockedReport, ReportRepository(dataSource),
                preferences, ImmediateScheduler(), connectionProvider)
        presenter.attachView(view)
    }

    @After
    fun finishing() {
        presenter.detachView()
    }

    @Test
    fun shouldNavigateBackWhenClickedOnImageButton() {
        presenter.onClickNavigateBack()

        verify(view).navigateBack()
    }

    @Test
    fun shouldExpandPopupMenuWhenClickedOnImageButton() {
        presenter.onClickPopupMenu()

        verify(view).showPopupMenu()
    }

    @Test
    fun shouldNavigateToCommentReportWhenClicked() {
        presenter.onClickCommentOnReport()

        verify(view).navigateToCommentReport()
    }

    @Test
    fun shouldSwapPageOnCarouselWhenSwipe() {
        presenter.swapPage(mockedIndicator)

        verify(view).swapPage(mockedIndicator)
    }

    @Test
    fun shouldLoadReportWithConnection() {
        `when`(connectionProvider.hasConnection())
                .thenReturn(true)

        `when`(preferences.getInt(User.ID))
                .thenReturn(1)

        `when`(dataSource.getReport(id = mockedReport.id, theme = mockedReport.theme,
                mapper = preferences.getInt(User.ID), status = mockedReport.status))
                .thenReturn(Single.just(mockedReport))

        presenter.loadReportData()

        verify(view).showLoading()
        verify(view).populateCarousel(presenter.getCarouselItems(mockedReport))
        verify(view).populateIndicator(presenter.getIndicators(mockedReport))
        verify(view).showReportData(mockedReport)
        verify(view).dismissLoading()
    }

    @Test
    fun shouldLoadReportWithoutConnection() {
        `when`(connectionProvider.hasConnection())
                .thenReturn(false)

        `when`(preferences.getInt(User.ID))
                .thenReturn(1)

        `when`(dataSource.getReport(id = mockedReport.id, theme = mockedReport.theme,
                mapper = preferences.getInt(User.ID), status = mockedReport.status))
                .thenReturn(Single.just(mockedReport))

        presenter.loadReportData()

        verify(view).showLoading()
        verify(view).populateCarousel(presenter.getCarouselItems(mockedReport))
        verify(view).populateIndicator(presenter.getIndicators(mockedReport))
        verify(view).showReportData(mockedReport)
        verify(view).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenLoadReportWithTimeout() {
        `when`(connectionProvider.hasConnection())
                .thenReturn(true)

        `when`(preferences.getInt(User.ID))
                .thenReturn(1)

        `when`(dataSource.getReport(id = mockedReport.id, theme = mockedReport.theme,
                mapper = preferences.getInt(User.ID), status = mockedReport.status))
                .thenReturn(emitSingleError(TimeoutException()))

        presenter.loadReportData()

        verify(view).showLoading()
        verify(view).showMessage(R.string.http_request_error)
        verify(view).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenLoadReportWithUnknownHost() {
        `when`(connectionProvider.hasConnection())
                .thenReturn(true)

        `when`(preferences.getInt(User.ID))
                .thenReturn(1)

        `when`(dataSource.getReport(id = mockedReport.id, theme = mockedReport.theme,
                mapper = preferences.getInt(User.ID), status = mockedReport.status))
                .thenReturn(emitSingleError(UnknownHostException()))

        presenter.loadReportData()

        verify(view).showLoading()
        verify(view).showMessage(R.string.login_network_error)
        verify(view).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenLoadReportWithNetworkError() {
        `when`(connectionProvider.hasConnection())
                .thenReturn(true)

        `when`(preferences.getInt(User.ID))
                .thenReturn(1)

        `when`(dataSource.getReport(id = mockedReport.id, theme = mockedReport.theme,
                mapper = preferences.getInt(User.ID), status = mockedReport.status))
                .thenReturn(emitSingleError(NetworkErrorException()))

        presenter.loadReportData()

        verify(view).showLoading()
        verify(view).showMessage(R.string.login_network_error)
        verify(view).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenLoadReportWithHttpException() {
        `when`(connectionProvider.hasConnection())
                .thenReturn(true)

        `when`(preferences.getInt(User.ID))
                .thenReturn(1)

        `when`(dataSource.getReport(id = mockedReport.id, theme = mockedReport.theme,
                mapper = preferences.getInt(User.ID), status = mockedReport.status))
                .thenReturn(emitSingleError(HttpException(
                        Response.error<ReportsApi>(500,
                                ResponseBody.create(MediaType.parse(""), ""))
                )))

        presenter.loadReportData()

        verify(view).showLoading()
        verify(view).showMessage(R.string.http_request_error)
        verify(view).dismissLoading()
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
            null,
            1,
            mutableListOf(),
            mutableListOf(),
            "lastNotification"
    )

}