package br.com.ilhasoft.voy.ui.addreport

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.models.ThemeData
import io.reactivex.Flowable
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

/**
 * Created by allan on 08/03/18.
 */
class ReportViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var interactor: AddReportInteractor

    private lateinit var viewModel: ReportViewModel

    private val mockedTheme = Theme(
            id = 1,
            name = "name",
            tags = listOf("try", "test"),
            color = "color",
            allowLinks = true
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        ThemeData.themeId = mockedTheme.id
        viewModel = ReportViewModel(interactor)
    }

    @Test
    fun `should set enabled button state`() {
        viewModel.setButtonEnable(true)
        assertTrue(viewModel.buttonEnable.value!!)

        viewModel.setButtonEnable(false)
        assertFalse(viewModel.buttonEnable.value!!)
    }

    @Test
    fun `should enable button when media is added`() {
        val media = getMedia()
        viewModel.addUri(media)

        assertTrue(viewModel.buttonEnable.value!!)
        assertEquals(media, viewModel.medias.first())
    }

    @Test
    fun `should disable button when media is removed`() {
        val media = getMedia()
        viewModel.addUri(media)
        viewModel.removeUri(media)

        assertFalse(viewModel.buttonEnable.value!!)
        assertTrue(viewModel.medias.isEmpty())
    }

    @Test
    fun `should set button title`() {
        viewModel.setButtonTitle(R.string.send)
        assertEquals(R.string.send, viewModel.buttonTitle.value)

        viewModel.setButtonTitle(R.string.next)
        assertEquals(R.string.next, viewModel.buttonTitle.value)
    }

    @Test
    fun `should add new link`() {
        val link = getLink()
        viewModel.addLink(link)

        assertTrue(viewModel.links.isNotEmpty())
        assertEquals(link, viewModel.links.first())

        viewModel.removeLink(link)
        viewModel.addLink(link)

        assertTrue(viewModel.links.isNotEmpty())
        assertEquals(link, viewModel.links.first())
    }

    @Test
    fun `should not add existent link`() {
        val link = getLink()
        viewModel.addLink(link)
        viewModel.addLink(link)

        assertEquals(1, viewModel.links.size)
    }

    @Test
    fun `should verify maximum quantity of links`() {
        viewModel.addLink(getLink(".com"))
        viewModel.addLink(getLink(".edu"))
        viewModel.addLink(getLink(".net"))

        assertTrue(viewModel.verifyListSize())

        viewModel.addLink(getLink(".org"))

        assertFalse(viewModel.verifyListSize())
    }

    @Test
    fun `should get all theme tags`() {
        `when`(interactor.getTags(mockedTheme.id))
                .thenReturn(Flowable.just(mockedTheme.tags.toMutableList()))

        assertEquals(mockedTheme.tags, viewModel.getAllTags().value)
    }

    @Test
    fun `should add selected tags`() {
        val tag1 = "try"
        val tag2 = "test"
        viewModel.addTag(tag1)
        viewModel.addTag(tag2)

        assertTrue(viewModel.selectedTags.isNotEmpty())
        assertEquals(2, viewModel.selectedTags.size)
    }

    @Test
    fun `should remove selected tag`() {
        val tag1 = "try"
        val tag2 = "test"
        viewModel.addTag(tag1)
        viewModel.addTag(tag2)
        viewModel.removeTag(tag1)

        assertTrue(viewModel.selectedTags.isNotEmpty())
        assertEquals(1, viewModel.selectedTags.size)
    }

    @Test
    fun `should check if a tag is selected`() {
        val tag = "test"
        viewModel.addTag(tag)

        assertTrue(viewModel.isTagSelected(tag))
    }

    @Test
    fun `should returns medias to delete`() {
        viewModel.medias.add(getMedia())
        viewModel.mediasFromServer.add(getReportFile(getMedia()))
        viewModel.mediasFromServer.add(getReportFile("file:///photo"))
        viewModel.mediasFromServer.add(getReportFile("file:///video"))

        assertTrue(viewModel.mediasToDelete().isNotEmpty())
        assertEquals(2, viewModel.mediasToDelete().size)
    }

    @Test
    fun `should returns medias to save`() {
        viewModel.medias.add(getMedia())
        viewModel.mediasFromServer.add(getReportFile("file:///photo"))
        viewModel.mediasFromServer.add(getReportFile("file:///video"))
        viewModel.mediasFromServer.add(getReportFile("file:///other_video"))

        assertTrue(viewModel.hasNewMedias())
        assertEquals(1, viewModel.mediasToSave().size)
    }

    @Test
    fun `should set up view model with report`() {
        val report = mock(Report::class.java)
        viewModel.setUpWithReport(report)

        assertEquals(report.files, viewModel.mediasFromServer)
        assertEquals(report.files.map { it.file }, viewModel.medias)
        assertEquals(report.name, viewModel.name)
        assertEquals(report.description, viewModel.description)
        assertEquals(report.urls, viewModel.links)
        assertEquals(report.tags, viewModel.selectedTags)
    }

    private fun getMedia() = "file:///media"

    private fun getLink(TLD: String = ".com") = "http://link$TLD"

    private fun getReportFile(file: String) = ReportFile(file = file)

}