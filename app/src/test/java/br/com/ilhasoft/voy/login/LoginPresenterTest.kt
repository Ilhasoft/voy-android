package br.com.ilhasoft.voy.login

import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.extensions.emitFlowableError
import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.authorization.AuthorizationDataSource
import br.com.ilhasoft.voy.network.authorization.AuthorizationRepository
import br.com.ilhasoft.voy.network.authorization.AuthorizationResponse
import br.com.ilhasoft.voy.network.users.UserDataSource
import br.com.ilhasoft.voy.network.users.UserRepository
import br.com.ilhasoft.voy.shared.schedulers.ImmediateScheduler
import br.com.ilhasoft.voy.ui.login.LoginContract
import br.com.ilhasoft.voy.ui.login.LoginPresenter
import io.reactivex.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeoutException

/**
 * Created by developer on 23/02/18.
 */
class LoginPresenterTest {

    @Mock
    private lateinit var authorizationDataSource: AuthorizationDataSource

    @Mock
    private lateinit var userDataSource: UserDataSource

    @Mock
    private lateinit var preferences: Preferences

    @Mock
    private lateinit var view: LoginContract
    private lateinit var presenter: LoginPresenter

    private val validCredentials = Credentials("pirralho", "123456")
    private val invalidCredentials = Credentials("unicef", "Unic@@f")
    private val emptyCredentials = Credentials("", "")
    private val token = "9d740e47daae2858da21563e4a89b8f5b7b9c502"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = LoginPresenter(AuthorizationRepository(authorizationDataSource),
                UserRepository(userDataSource), preferences, ImmediateScheduler())
        presenter.attachView(view)
    }

    @After
    fun finishing() {
        presenter.detachView()
    }

    @Test
    fun shouldEnableLoginButtonWhenValidFields() {
        Mockito.`when`(view.validate()).thenReturn(true)

        presenter.onClickLogin(validCredentials)

        verify(view).validate()
    }

    @Test
    fun shouldDisableLoginButtonWhenInvalidFields() {
        Mockito.`when`(view.validate()).thenReturn(false)

        presenter.onClickLogin(emptyCredentials)

        verify(view).validate()
    }

    @Test
    fun shouldMakeLoginWhenValidCredentials() {
        `when`(getLoginRef(validCredentials))
                .thenReturn(Flowable.just(AuthorizationResponse(token)))

        presenter.onClickLogin(validCredentials)

        verify(preferences).put(User.TOKEN, token)
        verify(userDataSource).getUser()
        verify(view).showMessage(R.string.login_success)
        verify(view).navigateToHome()
    }

    @Test
    fun shouldShowErrorMessageWhenMakeLoginWithInvalidCredentials() {
        `when`(getLoginRef(invalidCredentials))
                .thenReturn(Flowable.just(AuthorizationResponse("")))

        presenter.onClickLogin(invalidCredentials)

        verify(view).showMessage(R.string.invalid_user)
    }

    @Test
    fun shouldShowErrorMessageWhenMakeLoginWithTimeout() {
        `when`(getLoginRef(validCredentials))
                .thenReturn(emitFlowableError(TimeoutException()))

        presenter.onClickLogin(validCredentials)

        verify(view).showMessage(R.string.invalid_user)
    }

    private fun getLoginRef(credentials: Credentials): Flowable<AuthorizationResponse> =
            authorizationDataSource.loginWithCredentials(credentials)

}
