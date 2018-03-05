package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.shared.schedulers.ImmediateScheduler
import io.reactivex.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Created by Allan Lima on 05/03/2018.
 */
class HomePresenterTest {

    private lateinit var presenter: HomePresenter
    @Mock
    private lateinit var view: HomeContract
    @Mock
    private lateinit var interactor: HomeInteractor

    @Mock
    private lateinit var preferences: Preferences

    private val mockedUserId = 1
    private val mockedProject = Project(1, "", "", "", "", null, "", "", arrayListOf(), arrayListOf())
    private val mockedThemeList = mutableListOf(
            Theme(1, "", listOf(), "", listOf(), "", false),
            Theme(2, "", listOf(), "", listOf(), "", false),
            Theme(3, "", listOf(), "", listOf(), "", false)
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(preferences.getInt(User.ID)).thenReturn(mockedUserId)

        presenter = HomePresenter(preferences, interactor, ImmediateScheduler())
        presenter.attachView(view)
    }

    @After
    fun tearDown() {
        presenter.detachView()
    }

    @Test
    fun shouldLoadThemesDataWhenProjectIdIsValid() {
        `when`(interactor.getThemes(mockedProject.id, mockedUserId)).thenReturn(Flowable.just(mockedThemeList))

        presenter.onClickProject(mockedProject)

        verify(view).swapProject(mockedProject)
        verify(view).showLoading()
        verify(view).fillThemesAdapter(mockedThemeList)
        verify(view).dismissLoading()
    }

}