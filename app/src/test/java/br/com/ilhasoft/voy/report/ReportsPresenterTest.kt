package br.com.ilhasoft.voy.report

import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.models.Report
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
import java.lang.Exception

/**
 * Created by developer on 09/03/18.
 */
class ReportsPresenterTest {

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
    private val mockedMapperId = 1
    private val mockedReportList = listOf(mock(Report::class.java), mock(Report::class.java))
    private val viewModel = mock(ReportViewModel::class.java)

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = ReportsPresenter(ReportRepository(remoteDataSource, localDataSource,
            connectionProvider), ImmediateScheduler(), viewModel)
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
    fun shouldNavigateToAddReportWhenClickedOnImageButton() {
        presenter.onClickAddReport()

        verify(view).navigateToAddReport()
    }

    @Test
    fun `Should get reports from service when online`() {
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(remoteDataSource.getReports(theme = mockedThemeId, mapper = mockedMapperId))
            .thenReturn(Single.just(mockedReportList))
        `when`(localDataSource.saveReports(mockedReportList)).thenReturn(Single.just(mockedReportList))

        presenter.loadReports(mockedThemeId, mockedMapperId)

        verify(view).showLoading()
        verify(view).dismissLoading()
        verify(viewModel, times(3)).notifyReports(any(), any())
    }

    @Test
    fun `Should get reports from cache when offline`() {
        `when`(connectionProvider.hasConnection()).thenReturn(false)
        `when`(localDataSource.getReports(theme = mockedThemeId, mapper = mockedMapperId))
            .thenReturn(Single.just(mockedReportList))

        presenter.loadReports(mockedThemeId, mockedMapperId)

        verify(view).showLoading()
        verify(view).dismissLoading()
        verify(viewModel, times(3)).notifyReports(any(), any())
    }

    @Test
    fun `Should show an error message when something wrong`() {
        `when`(connectionProvider.hasConnection()).thenReturn(false)
        `when`(localDataSource.getReports(theme = mockedThemeId, mapper = mockedMapperId)).thenReturn(Single.error(Exception()))

        presenter.loadReports(mockedThemeId, mockedMapperId)

        verify(view).showLoading()
        verify(view).dismissLoading()
        verify(view).showMessage(R.string.report_list_error)
    }
}