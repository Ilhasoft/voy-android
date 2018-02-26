package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.network.authorization.AuthorizationResponse
import br.com.ilhasoft.voy.network.authorization.AuthorizationService
import br.com.ilhasoft.voy.network.users.UserChangeRequest
import br.com.ilhasoft.voy.network.users.UserService
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

/**
 * Created by erickjones on 26/02/18.
 */
class UserServiceTest {

    private lateinit var authService: AuthorizationService
    private lateinit var userService: UserService
    private lateinit var userChangeRequest: UserChangeRequest

    private val userMockId = 44
    private val userMockAvatar = "20"
    private val userMockPassword = null

    @Before
    fun setup() {
        authService = AuthorizationService()
        userService = UserService()
        userChangeRequest = UserChangeRequest(userMockId, userMockAvatar, userMockPassword)
    }

    @Test
    fun shouldReturnValidUser() {
        doLogin("pirralho", "123456")
                .concatMap { userService.getUser() }
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
    }

    @Test
    fun shouldReturnInvalidUser() {
        doLogin("invalid!?/", "unknown")
                .concatMap { userService.getUser() }
                .test()
                .assertSubscribed()
                .assertError(HttpException::class.java)
    }

    @Test
    fun shouldEditUser() {
        doLogin("jones", "123456")
                .map { userService.editUser(userChangeRequest) }
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
    }

    private fun doLogin(username: String, password: String): Flowable<AuthorizationResponse> =
        authService.loginWithCredentials(Credentials(username, password))
                .doOnNext { BaseFactory.accessToken = it.token }

}