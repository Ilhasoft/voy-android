package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.network.themes.ThemeDataSource
import br.com.ilhasoft.voy.network.themes.ThemeRepository
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeoutException

/**
 * Created by erickjones on 28/02/18.
 */
class ThemeServiceTest {

    @Mock
    lateinit var themeService: ThemeDataSource

    lateinit var themeRepository: ThemeRepository

    private val mockThemeObject = Theme(
            id = 22,
            project = "",
            bounds = mutableListOf(),
            name = "",
            tags = arrayListOf(),
            color = "",
            allowLinks = false)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        themeRepository = ThemeRepository(themeService)
    }

    @Test
    fun shouldReturnThemes() {
        `when`(themeService.getThemes()).thenReturn(Flowable.just(mutableListOf<Theme>()))

        themeRepository.getThemes()
                .test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors()
    }

    @Test
    fun shouldReturnThemeById() {
        `when`(themeService.getTheme(mockThemeObject.id)).thenReturn(Single.just(mockThemeObject))

         themeRepository.getTheme(mockThemeObject.id)
                .test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors()
                .assertValue { it.id == mockThemeObject.id}
    }

    @Test
    fun shouldNotReturnThemesTimeoutConnection() {
        `when`(themeService.getThemes())
                .thenReturn(Flowable.error(TimeoutException()))

        themeRepository.getThemes()
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

    @Test
    fun shouldNotReturnThemeTimeoutConnection() {
        `when`(themeService.getTheme(mockThemeObject.id))
                .thenReturn(Single.error(TimeoutException()))

        themeRepository.getTheme(mockThemeObject.id)
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

}