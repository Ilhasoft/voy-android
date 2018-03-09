package br.com.ilhasoft.voy.ui.addreport

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.ilhasoft.voy.R
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
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

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

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
        val media = "media"
        viewModel.addUri(media)

        assertTrue(viewModel.buttonEnable.value!!)
        assertEquals(viewModel.medias.first(), media)
    }

    @Test
    fun `should disable button when media is removed`() {
        val media = "media"
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
        val link = "link"
        viewModel.addLink(link)

        assertTrue(viewModel.links.isNotEmpty())
        assertEquals(viewModel.links.first(), link)

        viewModel.removeLink(link)
        viewModel.addLink(link)

        assertTrue(viewModel.links.isNotEmpty())
        assertEquals(viewModel.links.first(), link)
    }

    @Test
    fun `should not add existent link`() {
        val link = "link"
        viewModel.addLink(link)
        viewModel.addLink(link)

        assertEquals(viewModel.links.size, 1)
    }

}