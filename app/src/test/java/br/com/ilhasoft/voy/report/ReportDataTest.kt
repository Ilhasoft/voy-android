package br.com.ilhasoft.voy.report

import android.accounts.NetworkErrorException
import br.com.ilhasoft.voy.extensions.emitSingleError
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.models.User
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
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeoutException

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
    private val mockedUser = User(
        1,
        "firstName",
        "lastName",
        "language",
        "avatar",
        "username",
        "email",
        true,
        true,
        "passworg"
    )

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
    fun `should return a valid report response`() {
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

    @Test
    fun `should not return reports when there is no internet connection`() {
        `when`(
            reportService.getReports(
                mockPage,
                mockPageSize,
                mockTheme,
                mockProject,
                mockMapper,
                mockStatus
            )
        ).thenReturn(Single.error(UnknownHostException()))

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
            .assertError { it is UnknownHostException }
    }

    @Test
    fun `should not return reports when there is no server answer`() {
        `when`(
            reportService.getReports(
                mockPage,
                mockPageSize,
                mockTheme,
                mockProject,
                mockMapper,
                mockStatus
            )
        ).thenReturn(Single.error(TimeoutException()))

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
            .assertError { it is TimeoutException }
    }

    @Test
    fun `should not return reports when a bad request happens`() {
        `when`(
            reportService.getReports(
                mockPage,
                mockPageSize,
                mockTheme,
                mockProject,
                mockMapper,
                mockStatus
            )
        ).thenReturn(Single.error(NetworkErrorException()))

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
            .assertError { it is NetworkErrorException }
    }

    @Test
    fun `should return a single report`() {
        `when`(
            reportService.getReport(
                mockReportId,
                mockTheme,
                mockProject,
                mockMapper,
                mockStatus
            )
        )
            .thenReturn(Single.just(mockReport))

        reportRepository.getReport(mockReportId, mockTheme, mockProject, mockMapper, mockStatus)
            .test()
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun `should not return single report when the server is unreachable`() {
        `when`(
            reportService.getReport(
                mockReportId,
                mockTheme,
                mockProject,
                mockMapper,
                mockStatus
            )
        ).thenReturn(emitSingleError(UnknownHostException()))

        reportRepository.getReport(mockReportId, mockTheme, mockProject, mockMapper, mockStatus)
            .test()
            .assertSubscribed()
            .assertError { it is UnknownHostException }
    }

    @Test
    fun `should not return single report when there is no server answer`() {
        `when`(
            reportService.getReport(
                mockReportId,
                mockTheme,
                mockProject,
                mockMapper,
                mockStatus
            )
        ).thenReturn(emitSingleError(TimeoutException()))

        reportRepository.getReport(mockReportId, mockTheme, mockProject, mockMapper, mockStatus)
            .test()
            .assertSubscribed()
            .assertError { it is TimeoutException }
    }


    @Test
    fun `should not return single report when a bad request happens`() {
        `when`(
            reportService.getReport(
                mockReportId,
                mockTheme,
                mockProject,
                mockMapper,
                mockStatus
            )
        ).thenReturn(emitSingleError(NetworkErrorException()))

        reportRepository.getReport(mockReportId, mockTheme, mockProject, mockMapper, mockStatus)
            .test()
            .assertSubscribed()
            .assertError { it is NetworkErrorException }
    }

    @Test
    fun `should save report when the internet connection is available`() {
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
            mockDescription,
            mockName,
            createMockTagList(),
            createMockUrlList(),
            createMockFileList()
        ).test()
            .assertSubscribed()
            .assertNoErrors()
            .assertComplete()
            .assertValue { it == mockReport }

    }

    @Test
    fun `should not save report when there is no server answer`() {
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
        ).thenReturn(Observable.error(TimeoutException()))

        reportRepository.saveReport(
            mockTheme,
            mockLocation,
            mockDescription,
            mockName,
            createMockTagList(),
            createMockUrlList(),
            createMockFileList()
        ).test()
            .assertSubscribed()
            .assertError { it is TimeoutException }
    }

    @Test
    fun `should not save report when the server is unreachable`() {
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
        ).thenReturn(Observable.error(UnknownHostException()))

        reportRepository.saveReport(
            mockTheme,
            mockLocation,
            mockDescription,
            mockName,
            createMockTagList(),
            createMockUrlList(),
            createMockFileList()
        ).test()
            .assertSubscribed()
            .assertError { it is UnknownHostException }
    }

    @Test
    fun `should update report without removing files`() {
        `when`(
            reportService.updateReport(
                mockReportId,
                mockTheme,
                mockLocation,
                mockDescription,
                mockName,
                createMockTagList(),
                createMockUrlList(),
                createMockFileList(),
                null
            )
        ).thenReturn(Observable.just(mockReport))

        reportRepository.updateReport(
            mockReportId,
            mockTheme,
            mockLocation,
            mockDescription,
            mockName,
            createMockTagList(),
            createMockUrlList(),
            createMockFileList(),
            null
        ).test()
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue { it == mockReport }

    }

    @Test
    fun `should not update report when there is no server answer`() {
        `when`(
            reportService.updateReport(
                mockReportId,
                mockTheme,
                mockLocation,
                mockDescription,
                mockName,
                createMockTagList(),
                createMockUrlList(),
                createMockFileList(),
                null
            )
        ).thenReturn(Observable.error(TimeoutException()))

        reportRepository.updateReport(
            mockReportId,
            mockTheme,
            mockLocation,
            mockDescription,
            mockName,
            createMockTagList(),
            createMockUrlList(),
            createMockFileList(),
            null
        ).test()
            .assertSubscribed()
            .assertError { it is TimeoutException }
    }

    @Test
    fun `should not update report when the server is unreachable`() {
        `when`(
            reportService.updateReport(
                mockReportId,
                mockTheme,
                mockLocation,
                mockDescription,
                mockName,
                createMockTagList(),
                createMockUrlList(),
                createMockFileList(),
                null
            )
        ).thenReturn(Observable.error(UnknownHostException()))

        reportRepository.updateReport(
            mockReportId,
            mockTheme,
            mockLocation,
            mockDescription,
            mockName,
            createMockTagList(),
            createMockUrlList(),
            createMockFileList(),
            null
        ).test()
            .assertSubscribed()
            .assertError { it is UnknownHostException }
    }

    @Test
    fun `should not update report when a bad request happens`() {
        `when`(
            reportService.updateReport(
                mockReportId,
                mockTheme,
                mockLocation,
                mockDescription,
                mockName,
                createMockTagList(),
                createMockUrlList(),
                createMockFileList(),
                null
            )
        ).thenReturn(Observable.error(NetworkErrorException()))

        reportRepository.updateReport(
            mockReportId,
            mockTheme,
            mockLocation,
            mockDescription,
            mockName,
            createMockTagList(),
            createMockUrlList(),
            createMockFileList(),
            null
        ).test()
            .assertSubscribed()
            .assertError { it is NetworkErrorException }
    }

    @Test
    fun `should save report file`() {
        val mockFile = createMockFileList().first()
        val mockReportFile = createMockReportFile()

        `when`(reportService.saveFile(mockFile, mockReportId))
            .thenReturn(Single.just(mockReportFile))

        reportRepository.saveFile(mockFile, mockReportId)
            .test()
            .assertSubscribed()
            .assertNoErrors()
            .assertComplete()
            .assertValue { it == mockReportFile }
    }

    @Test
    fun `should not save file when the server is unreachable`() {
        val mockFile = createMockFileList().first()

        `when`(reportService.saveFile(mockFile, mockReportId))
            .thenReturn(emitSingleError(UnknownHostException()))

        reportRepository.saveFile(mockFile, mockReportId)
            .test()
            .assertSubscribed()
            .assertError { it is UnknownHostException }
    }

    @Test
    fun `should not save file when a bad request happens`() {
        val mockFile = createMockFileList().first()

        `when`(reportService.saveFile(mockFile, mockReportId))
            .thenReturn(emitSingleError(NetworkErrorException()))

        reportRepository.saveFile(mockFile, mockReportId)
            .test()
            .assertSubscribed()
            .assertError { it is NetworkErrorException }
    }

    @Test
    fun `should not save file when there is no server answer`() {
        val mockFile = createMockFileList().first()

        `when`(reportService.saveFile(mockFile, mockReportId))
            .thenReturn(emitSingleError(TimeoutException()))

        reportRepository.saveFile(mockFile, mockReportId)
            .test()
            .assertSubscribed()
            .assertError { it is TimeoutException }
    }

    private fun createMockResponse(): Response<Report> {
        return Response<Report>(
            1,
            "",
            "",
            listOf()
        )
    }

    private fun createMockReport(): Report = Report(
        1,
        1,
        mockLocation,
        true,
        true,
        true,
        Date(),
        "description",
        "name",
        mutableListOf(),
        "themeColor",
        mockedUser,
        null,
        1,
        mutableListOf(),
        mutableListOf(),
        "lastNotification"
    )

    private fun createMockReportFile() =
        ReportFile(
            1,
            "",
            "",
            "",
            "",
            null,
            0
        )

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