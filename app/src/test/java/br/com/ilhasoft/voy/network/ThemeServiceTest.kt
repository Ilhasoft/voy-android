package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.network.themes.ThemeApi
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * Created by erickjones on 28/02/18.
 */
class ThemeServiceTest {

    lateinit var themeService: MockThemeService
    var mockThemeId = 22

    @Before
    fun setup() {
        themeService = MockThemeService()
    }

    @Test
    fun shouldReturnThemes() {
        themeService.getThemes()
                .test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors()
                .assertValue { themesList -> themesList.isNotEmpty() }
    }

    @Test
    fun shouldReturnThemeById() {
         themeService.getTheme(mockThemeId)
                .test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors()
                .assertValue { it.id == mockThemeId }
    }

    @Test
    fun shouldNotReturnThemesUnknownHost() {
        themeService.getThemes()
                .test()
                .assertSubscribed()
                .assertError { it is UnknownHostException }
    }

    @Test
    fun shouldNotReturnThemesTimeoutConnection() {
        themeService.getThemes()
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

        @Test
    fun shouldNotReturnThemeUnknownHost() {
        themeService.getTheme(mockThemeId)
                .test()
                .assertSubscribed()
                .assertError { it is UnknownHostException }
    }

    @Test
    fun shouldNotReturnThemeTimeoutConnection() {
        themeService.getTheme(mockThemeId)
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

}

class MockThemeService : ServiceFactory<ThemeApi>(ThemeApi::class.java) {

    private val mockThemeObject = Theme(id = 22,
            project = "",
            bounds = mutableListOf(),
            name = "",
            tags = arrayListOf(),
            color = "",
            allowLinks = false)

    fun getThemes(): Flowable<MutableList<Theme>> =
            Flowable.just(mutableListOf<Theme>())

    fun getTheme(themeId: Int): Single<Theme> =
            Single.just(mockThemeObject)

}