package br.com.ilhasoft.voy.report

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.report.ReportStatus
import br.com.ilhasoft.voy.ui.report.ReportViewModel
import org.junit.Assert.assertNotNull
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

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = ReportViewModel()
    }

    @Test
    fun shouldLoadReportsWhenHavingApprovedReports() {
        viewModel.approvedReports.value = createMockedApprovedReports()
        viewModel.getReports(ReportStatus.APPROVED)
        assertNotNull(viewModel.getReports(ReportStatus.APPROVED))
    }

    @Test
    fun shouldLoadReportsWhenHavingPendingReports() {
        viewModel.pendingReports.value = createMockedPendingReports()
        viewModel.getReports(ReportStatus.PENDING)
        assertNotNull(viewModel.getReports(ReportStatus.PENDING))
    }

    @Test
    fun shouldLoadReportsWhenHavingUnapprovedReports() {
        viewModel.unApprovedReports.value = createMockedUnapprovedReports()
        viewModel.getReports(ReportStatus.UNAPPROVED)
        assertNotNull(viewModel.getReports(ReportStatus.UNAPPROVED))
    }

    private fun createMockedApprovedReports() = mutableListOf(Report(id = 100, status = 1))

    private fun createMockedPendingReports() = mutableListOf(Report(id = 200, status = 2))

    private fun createMockedUnapprovedReports() = mutableListOf(Report(id = 300, status = 3))

}