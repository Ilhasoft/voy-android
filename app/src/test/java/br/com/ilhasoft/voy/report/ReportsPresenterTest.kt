package br.com.ilhasoft.voy.report

import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.ui.report.ReportsContract
import br.com.ilhasoft.voy.ui.report.ReportsPresenter
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

/**
 * Created by developer on 09/03/18.
 */
class ReportsPresenterTest {

    @Mock
    private lateinit var view: ReportsContract
    private lateinit var presenter: ReportsPresenter
    private val mockedInvalidDateToReport = createFakeDate(6, 1, 2018)
    private val mockedValidDateToReport = createFakeDate(2, 1, 2018)
    private val mockedThemeStartAt = createFakeDate(1, 1, 2018)
    private val mockedThemeEndAt = createFakeDate(5, 1, 2018)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = ReportsPresenter()
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

    private fun createFakeDate(day: Int, month: Int, year: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.time
    }
}