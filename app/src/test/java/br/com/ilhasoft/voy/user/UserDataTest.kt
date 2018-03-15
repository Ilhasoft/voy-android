package br.com.ilhasoft.voy.user

import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.users.UserChangeRequest
import br.com.ilhasoft.voy.network.users.UserDataSource
import br.com.ilhasoft.voy.network.users.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeoutException

/**
 * Created by erickjones on 26/02/18.
 */
class UserDataTest {

    @Mock
    lateinit var userService: UserDataSource

    lateinit var userRepository: UserRepository

    private var mockUserId = 1

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        userRepository = UserRepository(userService)
    }

    @Test
    fun shouldReturnUserFromAPI() {
        `when`(userService.getUser()).thenReturn(Single.just(createMockUser()))

        userRepository.getUser()
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue { it.id == mockUserId }
    }

    @Test
    fun shouldNotReturnUserFromAPI() {
        `when`(userService.getUser()).thenReturn(Single.error(TimeoutException()))

        userRepository.getUser()
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

    @Test
    fun shouldEditUser() {
        `when`(userService.editUser(createMockUserChangeRequest()))
                .thenReturn(Completable.complete())

        userRepository.editUser(createMockUserChangeRequest())
                .test()
                .assertSubscribed()
                .assertComplete()
                .assertNoErrors()
    }

    @Test
    fun shouldNotEditUser() {
         `when`(userService.editUser(createMockUserChangeRequest()))
                .thenReturn(Completable.error(TimeoutException()))

        userRepository.editUser(createMockUserChangeRequest())
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

    private fun createMockUser(): User =
        User(mockUserId, "2", "mockeduser", "mockuser@test.test")


    private fun createMockUserChangeRequest() =
            UserChangeRequest(mockUserId, "2", "usermockchange@test.test")



}
