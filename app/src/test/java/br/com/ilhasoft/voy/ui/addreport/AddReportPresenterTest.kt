package br.com.ilhasoft.voy.ui.addreport

import br.com.ilhasoft.voy.models.AddReportFragmentType
import br.com.ilhasoft.voy.models.Report
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Created by allan on 12/03/2018.
 */
class AddReportPresenterTest {

    @Mock
    private lateinit var view: AddReportContract
    @Mock
    private lateinit var interactor: AddReportInteractor

    private lateinit var presenter: AddReportPresenter
    private lateinit var report: Report
    private lateinit var viewModel: ReportViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        report = Report(name = "name", description = "description")
        viewModel = ReportViewModel(interactor)

        presenter = AddReportPresenter(viewModel, listOf(), report, interactor)
        presenter.attachView(view)
    }

    @After
    fun tearDown() {
        presenter.detachView()
    }

    @Test
    fun `should set up report and navigate to medias fragment when view is attached`() {
        assertEquals(report.name, viewModel.name)
        assertEquals(report.description, viewModel.description)
        verify(view).navigateToNext(AddReportFragmentType.MEDIAS)
    }

    @Test
    fun `should check location when presenter on resume`() {
        presenter.resume()

        assertTrue(presenter.requestingUpdates)
        verify(view).checkLocation()
    }

    @Test
    fun `should stop getting location when on pause`() {
        presenter.pause()

        assertFalse(presenter.requestingUpdates)
        verify(view).stopGettingLocation()
    }

    @Test
    fun `should back when on click navigate back`() {
        presenter.onClickNavigateBack()

        verify(view).navigateBack()
    }

    @Test
    fun `should navigate to next fragment when on click navigate next`() {
        `when`(view.getVisibleFragmentType()).thenReturn(AddReportFragmentType.MEDIAS)
        presenter.onClickNavigateNext()
        verify(view).navigateToNext(AddReportFragmentType.TITLE)

        `when`(view.getVisibleFragmentType()).thenReturn(AddReportFragmentType.TITLE)
        presenter.onClickNavigateNext()
        verify(view).navigateToNext(AddReportFragmentType.TAG)
    }

}