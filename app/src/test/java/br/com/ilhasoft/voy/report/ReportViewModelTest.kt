package br.com.ilhasoft.voy.report

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.report.ReportStatus
import br.com.ilhasoft.voy.ui.report.ReportViewModel
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

/**
 * Created by developer on 13/03/18.
 */
class ReportViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ReportViewModel
    private val mockedApprovedReports = mutableListOf(Report(id = 100, status = 1))
    private val mockedPendingReports = mutableListOf(Report(id = 200, status = 2))
    private val mockedUnapprovedReports = mutableListOf(Report(id = 300, status = 3))

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = ReportViewModel()
    }

    @Test
    fun shouldLoadReportsWhenHavingApprovedReports() {
        viewModel.notifyReports(mockedApprovedReports, ReportStatus.APPROVED)
        val newApprovedReports = viewModel.getReports(ReportStatus.APPROVED)
        assertNotNull(newApprovedReports)
        assertTrue(newApprovedReports.value == mockedApprovedReports)
    }

    @Test
    fun shouldLoadReportsWhenHavingPendingReports() {
        viewModel.notifyReports(mockedPendingReports, ReportStatus.PENDING)
        val newPendingReports = viewModel.getReports(ReportStatus.PENDING)
        assertNotNull(newPendingReports)
        assertTrue(newPendingReports.value == mockedPendingReports)
    }

    @Test
    fun shouldLoadReportsWhenHavingUnapprovedReports() {
        viewModel.notifyReports(mockedUnapprovedReports, ReportStatus.UNAPPROVED)
        val newUnapprovedReports = viewModel.getReports(ReportStatus.UNAPPROVED)
        assertNotNull(newUnapprovedReports)
        assertTrue(newUnapprovedReports.value == mockedUnapprovedReports)
    }
}