package br.com.ilhasoft.voy.login

import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.network.authorization.AuthorizationDataSource
import br.com.ilhasoft.voy.network.authorization.AuthorizationRepository
import br.com.ilhasoft.voy.network.authorization.AuthorizationResponse
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Created by developer on 06/03/18.
 */
class LoginDataTest {

    @Mock
    private lateinit var dataSource: AuthorizationDataSource
    private lateinit var authorizationRepository: AuthorizationRepository

    private val validCredentials = Credentials("pirralho", "123456")
    private val invalidCredentials = Credentials("unicef", "Unic@@f")
    private val token = "9d740e47daae2858da21563e4a89b8f5b7b9c502"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        authorizationRepository = AuthorizationRepository(dataSource)
    }

    @Test
    fun shouldMakeLoginWhenValidCredentials() {
        `when`(dataSource.loginWithCredentials(validCredentials))
                .thenReturn(Flowable.just(AuthorizationResponse(token)))

        authorizationRepository.loginWithCredentials(validCredentials)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue { (token) -> token.isNotEmpty() }
    }

    @Test
    fun shouldNotMakeLoginWhenInvalidCredentials() {
        `when`(dataSource.loginWithCredentials(invalidCredentials))
                .thenReturn(Flowable.just(AuthorizationResponse("")))

        authorizationRepository.loginWithCredentials(invalidCredentials)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue { (token) -> token.isEmpty() }
    }

}