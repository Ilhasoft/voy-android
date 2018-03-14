package br.com.ilhasoft.voy.ui.addreport

import android.location.Location
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.db.report.ReportFileDbModel
import br.com.ilhasoft.voy.models.AddReportFragmentType
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.shared.helpers.LocationHelpers
import br.com.ilhasoft.voy.shared.schedulers.ImmediateScheduler
import io.reactivex.Observable
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Created by allan on 12/03/2018.
 */
class AddReportPresenterTest {

    @Mock
    private lateinit var view: AddReportContract
    @Mock
    private lateinit var interactor: AddReportInteractor
    @Mock
    private lateinit var mockedLocation: Location

    private lateinit var presenter: AddReportPresenter
    private lateinit var viewModel: ReportViewModel
    private lateinit var mockedReport: Report

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        mockedReport = Report(name = "name", description = "description", location = br.com.ilhasoft.voy.models.Location("", arrayListOf()))
        viewModel = ReportViewModel(interactor)

        presenter = AddReportPresenter(viewModel, listOf(), mockedReport, interactor, ImmediateScheduler())
        presenter.attachView(view)
    }

    @After
    fun tearDown() {
        presenter.detachView()
    }

    @Test
    fun `should set up report and navigate to medias fragment when view is attached`() {
        assertEquals(mockedReport.name, viewModel.name)
        assertEquals(mockedReport.description, viewModel.description)
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

    // TODO try refactor concrete implementations to test better
    /*
    @Test
    fun `should add report when is final step and user is inside bounds`() {

        `when`(mockedLocation.longitude).thenReturn(0.0)
        `when`(mockedLocation.latitude).thenReturn(0.0)
        `when`(mock(LocationHelpers::class.java).isLocationInsidePolygon(mockedLocation.latitude, mockedLocation.longitude, listOf()))
                .thenReturn(Observable.just(true))
        presenter.isFinalStep = true
        viewModel.report.id = 0

        presenter.onLocationLoaded(mockedLocation)

        verify(view).dismissLoadLocationDialog()
        verify(view).showLoading()
        verify(view).dismissLoading()
        verify(view).navigateToThanks()
    }*/

    @Test
    fun `should not add report when is final step and user is outside bounds`() {
        `when`(mockedLocation.longitude).thenReturn(0.0)
        `when`(mockedLocation.latitude).thenReturn(0.0)
        `when`(mock(LocationHelpers::class.java).isLocationInsidePolygon(mockedLocation.latitude, mockedLocation.longitude, listOf()))
                .thenReturn(Observable.just(false))
        presenter.isFinalStep = true
        viewModel.report.id = 0

        presenter.onLocationLoaded(mockedLocation)

        verify(view).dismissLoadLocationDialog()
        verify(view).showMessage(R.string.outside_theme_bounds)
        assertFalse(presenter.isFinalStep)
    }

    @Test
    fun `should update report when is final step and there view model report`() {
        mockedReport.name = "new report name"

        `when`(mockedLocation.longitude).thenReturn(0.0)
        `when`(mockedLocation.latitude).thenReturn(0.0)
        `when`(mock(LocationHelpers::class.java).isLocationInsidePolygon(mockedLocation.latitude, mockedLocation.longitude, listOf()))
                .thenReturn(Observable.just(true))
        `when`(interactor.updateReport(
                mockedReport.internalId,
                mockedReport.id,
                mockedReport.theme,
                mockedReport.location!!,
                mockedReport.description,
                mockedReport.name,
                viewModel.selectedTags,
                viewModel.links,
                viewModel.medias.map { ReportFileDbModel().apply { file = it } }.toMutableList(),
                mutableListOf(),
                viewModel.mediasToDelete()

        )).thenReturn(Observable.just(mockedReport))
        presenter.isFinalStep = true
        viewModel.report.id = 1

        presenter.onLocationLoaded(mockedLocation)

        verify(view).dismissLoadLocationDialog()
        verify(view).showLoading()
        verify(view).dismissLoading()
    }

    @Test
    fun `should not save report when user is not on final step of save report and is outside theme bounds`() {
        `when`(mockedLocation.longitude).thenReturn(0.0)
        `when`(mockedLocation.latitude).thenReturn(0.0)
        `when`(mock(LocationHelpers::class.java).isLocationInsidePolygon(mockedLocation.latitude, mockedLocation.longitude, listOf()))
                .thenReturn(Observable.just(false))
        presenter.isFinalStep = false
        viewModel.report.id = 0

        presenter.onLocationLoaded(mockedLocation)

        verify(view).dismissLoadLocationDialog()
        verify(view).showOutsideDialog()
        verify(view).stopGettingLocation()
        assertFalse(presenter.requestingUpdates)
    }

}