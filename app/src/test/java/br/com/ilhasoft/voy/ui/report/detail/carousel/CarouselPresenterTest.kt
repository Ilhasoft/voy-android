package br.com.ilhasoft.voy.ui.report.detail.carousel

import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.models.User
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Created by felipe on 09/03/18.
 */
class CarouselPresenterTest {

    @Mock
    private lateinit var carouselContract: CarouselContract
    private lateinit var carouselPresenter: CarouselPresenter
    private val mockedUser: User = User(1, "avatar", "username", "email")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        carouselPresenter = CarouselPresenter()
        carouselPresenter.attachView(carouselContract)
    }

    @After
    fun tearDown() {
        carouselPresenter.detachView()
    }

    @Test
    fun `Should display video when mediaType is a video type`() {
        val mockedVideoReportFile = createFakeReportFile("video")

        carouselPresenter.displayMedia(mockedVideoReportFile)

        verify(carouselContract).displayVideo(mockedVideoReportFile.file)
    }

    @Test
    fun `Should display image when mediaType is a image type`() {
        val mockedVideoReportFile = createFakeReportFile("image")

        carouselPresenter.displayMedia(mockedVideoReportFile)

        verify(carouselContract).displayImage(mockedVideoReportFile.file)
    }

    private fun createFakeReportFile(type: String): ReportFile {
        return ReportFile(1, "title", "description", type, "file", mockedUser, 1)
    }
}