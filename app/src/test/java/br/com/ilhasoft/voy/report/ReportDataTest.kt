package br.com.ilhasoft.voy.report

import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.reports.ReportDataSource
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.network.reports.Response
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.io.File

/**
 * Created by erickjones on 06/03/18.
 */
class ReportDataTest {

    @Mock
    lateinit var reportService: ReportDataSource

    lateinit var reportRepository: ReportRepository

    private val mockReportId = 1
    private val mockPage = 1
    private val mockPageSize = 50
    private val mockTheme = 1
    private val mockProject = 1
    private val mockMapper = 1
    private val mockStatus = 1
    private val mockName = "FakeName"
    private val mockDescription = "FakeDescription"
    private val mockLocation = Location("GPS", arrayListOf(1.2, 2.2))

    lateinit var mockReponse: Response<Report>
    lateinit var mockReport: Report


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        reportRepository = ReportRepository(reportService)
        mockReponse = createMockResponse()
        mockReport = createMockReport()
    }

    @Test
    fun shouldGetReportsResponse() {
        `when`(
            reportService.getReports(
                mockPage,
                mockPageSize,
                mockTheme,
                mockProject,
                mockMapper,
                mockStatus
            )
        ).thenReturn(Single.just(mockReponse))

        reportRepository.getReports(
            mockPage,
            mockPageSize,
            mockTheme,
            mockProject,
            mockMapper,
            mockStatus
        )
            .test()
            .assertSubscribed()
            .assertNoErrors()
            .assertValue { it == mockReponse }

    }

    //FIXME throwing NPE when trying to call test()
    @Test
    fun shouldGetSingleReport() {
        `when`(reportService.getReport(mockReportId, mockTheme, mockMapper, mockStatus))
            .thenReturn(Single.just(mockReport))

        reportRepository.getReport(mockReportId, mockTheme, mockProject, mockMapper, mockStatus)
            .test()
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
    }

    //FIXME throwing NPE when trying to call test()
    @Test
    fun shouldSaveReportWhenNetworkIsAvailable() {
        val mockReport = createMockReport()

        `when`(
            reportService.saveReport(
                mockTheme,
                mockLocation,
                mockDescription,
                mockName,
                createMockTagList(),
                createMockUrlList(),
                createMockFileList()
            )
        ).thenReturn(Observable.just(mockReport))

        reportRepository.saveReport(
            mockTheme,
            mockLocation,
            "",
            "",
            listOf(),
            listOf(),
            listOf()
        ).test()
            .assertSubscribed()
            .assertNoErrors()
            .assertComplete()
            .assertValue { it == mockReport }

    }

    private fun createMockResponse(): Response<Report> {
        return Response<Report>(
            1,
            "",
            "",
            listOf()
        )
    }

    private fun createMockReport(): Report = Report()


    private fun createMockTagList(): MutableList<String> =
        mutableListOf(
            "FakeTag1",
            "FakeTag2",
            "FakeTag3"
        )

    private fun createMockUrlList(): MutableList<String> =
        mutableListOf(
            "FakeUrl1",
            "FakeUrl2",
            "FakeUrl3"
        )

    private fun createMockFileList(): MutableList<File> =
        mutableListOf(
            File("FakeFile1"),
            File("FakeFile2"),
            File("FakeFile3")
        )

}
