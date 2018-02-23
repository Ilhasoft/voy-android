package br.com.ilhasoft.voy

import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.network.authorization.AuthorizationResponse
import br.com.ilhasoft.voy.network.authorization.AuthorizationService
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

/**
 * Created by developer on 23/02/18.
 */
class LoginTest {

    private lateinit var service: AuthorizationService

    @Before
    fun setup() {
        service = AuthorizationService()
    }

    @Test
    fun tryLoginWithValidCredentials() {
        setupLoginCredentials(Credentials("pirralho", "123456"))
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
    }

    @Test
    fun tryLoginWithInvalidCredentials() {
        setupLoginCredentials(Credentials("unicef", "Unic@@f"))
                .test()
                .assertSubscribed()
                .assertError(HttpException::class.java)
    }

    @Test
    fun checkTokenReturnOnLogin() {
        setupLoginCredentials(Credentials("pirralho", "123456"))
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue { (token) -> token.isNotEmpty() }
    }

    private fun setupLoginCredentials(credentials: Credentials): Flowable<AuthorizationResponse> =
            service.loginWithCredentials(credentials)

}
