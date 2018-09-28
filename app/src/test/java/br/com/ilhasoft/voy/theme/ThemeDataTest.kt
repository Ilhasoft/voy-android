package br.com.ilhasoft.voy.theme

import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.network.themes.ThemeDataSource
import br.com.ilhasoft.voy.network.themes.ThemeRepository
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeoutException

/**
 * Created by erickjones on 28/02/18.
 */
class ThemeDataTest {

    @Mock
    lateinit var themeRemoteDataSource: ThemeDataSource
    @Mock
    lateinit var themeLocalDataSource: ThemeDataSource
    @Mock
    lateinit var connectionProvider: CheckConnectionProvider

    lateinit var themeRepository: ThemeRepository

    private val mockThemeObject = mock(Theme::class.java)
    private val mockThemeList = mutableListOf(mock(Theme::class.java), mock(Theme::class.java))
    private val mockedLanguage = "en"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        themeRepository =
                ThemeRepository(themeRemoteDataSource, themeLocalDataSource, connectionProvider)
    }

    @Test
    fun shouldReturnThemesWhenOnline() {
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(themeRemoteDataSource.getThemes(lang=mockedLanguage)).thenReturn(Flowable.just(mockThemeList))

        themeRepository.getThemes(lang = mockedLanguage)
            .test()
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValues(mockThemeList)
    }

    @Test
    fun shouldReturnThemeById() {
        `when`(themeRemoteDataSource.getTheme(mockThemeObject.id, lang = mockedLanguage)).thenReturn(
            Single.just(
                mockThemeObject
            )
        )

        themeRepository.getTheme(mockThemeObject.id, lang = mockedLanguage)
            .test()
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue { it.id == mockThemeObject.id }
    }

    @Test
    fun shouldNotReturnThemesTimeoutConnection() {
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(themeRemoteDataSource.getThemes(lang = mockedLanguage))
            .thenReturn(Flowable.error(TimeoutException()))

        themeRepository.getThemes(lang = mockedLanguage)
            .test()
            .assertSubscribed()
            .assertError { it is TimeoutException }
    }

    @Test
    fun shouldNotReturnThemeTimeoutConnection() {
        `when`(themeRemoteDataSource.getTheme(mockThemeObject.id, lang = mockedLanguage))
            .thenReturn(Single.error(TimeoutException()))

        themeRepository.getTheme(mockThemeObject.id, lang = mockedLanguage)
            .test()
            .assertSubscribed()
            .assertError { it is TimeoutException }
    }
}