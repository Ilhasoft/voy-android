package br.com.ilhasoft.voy.report.detail

import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.models.Indicator
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.reports.ReportDataSource
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.shared.schedulers.ImmediateScheduler
import br.com.ilhasoft.voy.ui.report.detail.ReportDetailContract
import br.com.ilhasoft.voy.ui.report.detail.ReportDetailPresenter
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Created by developer on 08/03/18.
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

    private val mockedReport = Report()
    private val mockedIndicator = Indicator()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = ReportDetailPresenter(mockedReport, ReportRepository(dataSource),
                preferences, ImmediateScheduler(), connectionProvider)
        presenter.attachView(view)
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

}