package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.network.users.UserChangeRequest
import br.com.ilhasoft.voy.network.users.UserService
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

/**
 * Created by erickjones on 26/02/18.
 */
class UserServiceTest {

    private lateinit var userService: UserService
    private lateinit var userChangeRequest: UserChangeRequest

    private var mockUserId = 44
    private var mockUserAvatar = "22"
    private var mockUserUsername = "jones"
    private var mockUserPassword = "123456"
    private var mockUserToken = "77722a7b1c8491e12701a32b6b1c067f911492ce"

    @Before
    fun setup() {
        userService = UserService()
        BaseFactory.accessToken = mockUserToken
        userChangeRequest = UserChangeRequest(mockUserId, mockUserAvatar, mockUserPassword)
    }

    @Test
    fun shouldReturnLoggedUserFromAPI() {
        userService.getUser()
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue { it.id == mockUserId }
    }

    @Test
    fun shouldNotReturnLoggedUserFromAPI() {
        userService.getUser()
                .test()
                .assertSubscribed()
                .assertError(HttpException::class.java)
    }

    @Test
    fun shouldEditUser() {
        userService.editUser(userChangeRequest)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
        //TODO change to verify the api returned changes
    }

    @Test
    fun shouldNotEditUser() {
        userService.editUser(userChangeRequest)
                .test()
                .assertSubscribed()
                .assertError(HttpException::class.java)
    }

}