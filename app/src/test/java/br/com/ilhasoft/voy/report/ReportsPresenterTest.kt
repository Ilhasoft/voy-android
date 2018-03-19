package br.com.ilhasoft.voy.report

import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.reports.ReportDataSource
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.shared.schedulers.ImmediateScheduler
import br.com.ilhasoft.voy.ui.report.ReportViewModel
import br.com.ilhasoft.voy.ui.report.ReportsContract
import br.com.ilhasoft.voy.ui.report.ReportsPresenter
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*

/**
 * Created by developer on 09/03/18.
 */
class ReportsPresenterTest {

    @Mock
    private lateinit var preferences: Preferences

    @Mock
    private lateinit var remoteDataSource: ReportDataSource

    @Mock
    private lateinit var localDataSource: ReportDataSource

    @Mock
    private lateinit var connectionProvider: CheckConnectionProvider

    @Mock
    private lateinit var view: ReportsContract
    private lateinit var presenter: ReportsPresenter
    private val mockedThemeId = 1
    private val mockedStatusId = 1
    private val mockedMapperId = 1
    private val mockedPage = "1"
    private val mockedPageSize = 50
    private val mockedList = listOf(mock(Report::class.java), mock(Report::class.java))
    private val viewModel = mock(ReportViewModel::class.java)
    private val mockedInvalidDateToReport = createFakeDate(6, 1, 2018)
    private val mockedValidDateToReport = createFakeDate(2, 1, 2018)
    private val mockedThemeStartAt = createFakeDate(1, 1, 2018)
    private val mockedThemeEndAt = createFakeDate(5, 1, 2018)

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = ReportsPresenter(
            preferences,
            ReportRepository(
                remoteDataSource,
                localDataSource,
                connectionProvider
            ), ImmediateScheduler(), viewModel
        )
        presenter.attachView(view)
        ThemeData.startAt = mockedThemeStartAt
        ThemeData.endAt = mockedThemeEndAt
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
    fun shouldNavigateToAddReportWhenClickedOnImageButtonIfValidDate() {
        presenter.onClickAddReport(mockedValidDateToReport)

        verify(view).navigateToAddReport()
    }

    @Test
    fun shouldNavigateToAddReportWhenClickedOnImageButtonIfInvalidDate() {
        presenter.onClickAddReport(mockedInvalidDateToReport)

        verify(view).showMessage(R.string.period_ended_text)
    }

    @Test
    fun `Should get reports from service when online`() {
        `when`(preferences.getInt(User.ID)).thenReturn(mockedMapperId)
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(remoteDataSource.getReports(mockedThemeId,null, mockedMapperId,
            mockedStatusId, mockedPage.toInt(), mockedPageSize))
            .thenReturn(Single.just(mockedPage to mockedList))
        `when`(localDataSource.saveReports(mockedList)).thenReturn(Single.just(mockedList))

        presenter.loadReports(mockedThemeId, mockedStatusId, mockedPage.toInt(), mockedPageSize)

        verify(view).showLoading()
        verify(view).dismissLoading()
        verify(viewModel).notifyOnDemand(mockedPage)
        verify(viewModel, times(3)).notifyReports(any(), any())
    }

    @Test
    fun `Should get reports from cache when offline`() {
        `when`(preferences.getInt(User.ID)).thenReturn(mockedMapperId)
        `when`(connectionProvider.hasConnection()).thenReturn(false)
        `when`(
            localDataSource.getReports(theme = mockedThemeId, mapper = mockedMapperId, status = mockedStatusId)
        ).thenReturn(Single.error(Exception()))

        presenter.loadReports(mockedThemeId, mockedStatusId, mockedPage.toInt())

        verify(view).showLoading()
        verify(view).dismissLoading()
        verify(view).showMessage(R.string.report_list_error)
    }

    @Test
    fun `Should show an error message when something wrong`() {
        `when`(preferences.getInt(User.ID)).thenReturn(mockedMapperId)
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(
            remoteDataSource.getReports(
                mockedThemeId,null, mockedMapperId, mockedStatusId, mockedPage.toInt(), mockedPageSize
            )
        ).thenReturn(Single.error(Exception()))

        presenter.loadReports(mockedThemeId, mockedStatusId, mockedPage.toInt(), mockedPageSize)

        verify(view).showLoading()
        verify(view).dismissLoading()
        verify(view).showMessage(R.string.report_list_error)
    }

    private fun createFakeDate(day: Int, month: Int, year: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.time
    }
}