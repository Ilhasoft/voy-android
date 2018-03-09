package br.com.ilhasoft.voy.report

import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.network.reports.ReportDataSource
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.ui.report.ReportsContract
import br.com.ilhasoft.voy.ui.report.ReportsPresenter
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

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

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = ReportsPresenter(ReportRepository(remoteDataSource, localDataSource, connectionProvider))
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

}