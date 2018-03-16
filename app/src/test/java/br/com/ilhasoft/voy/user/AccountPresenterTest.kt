package br.com.ilhasoft.voy.user

import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.users.UserApi
import br.com.ilhasoft.voy.ui.account.AccountContract
import br.com.ilhasoft.voy.ui.account.AccountInteractor
import br.com.ilhasoft.voy.ui.account.AccountPresenter
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

/**
 * Created by erickjones on 02/03/18.
 */
class AccountPresenterTest {

    @Mock
    lateinit var accountInteractor: AccountInteractor

    @Mock
    lateinit var accountView: AccountContract

    lateinit var accountPresenter: AccountPresenter
    lateinit var mockedUser: User

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        accountPresenter = AccountPresenter(accountInteractor)
        accountPresenter.attachView(accountView)
        mockedUser = createMockUser()
    }

    @Test
    fun shouldLoadAndDisplayUser() {
        `when`(accountInteractor.getUser()).thenReturn(Single.just(mockedUser))

        accountPresenter.start()

        verify(accountView).setUser(mockedUser)
    }

    @Test
    fun `should not load user when trying to request offline`() {
        `when`(accountInteractor.getUser()).thenReturn(Single.error(UnknownHostException()))

        accountPresenter.start()

        verify(accountView).showMessage(R.string.login_network_error)
    }

    @Test
    fun `should not load user when a bad request happens`() {
        `when`(accountInteractor.getUser())
            .thenReturn(
                Single.error(
                    HttpException(
                        Response.error<UserApi>(
                            403,
                            ResponseBody.create(
                                MediaType.parse("application/json"),
                                "{}"
                            )
                        )
                    )
                )
            )

        accountPresenter.start()

        verify(accountView).showMessage(R.string.http_request_error)
    }

    @Test
    fun `should not save user when a server error occur`() {
        `when`(accountInteractor.getUser())
            .thenReturn(
                Single.error(
                    HttpException(
                        Response.error<UserApi>(
                            500,
                            ResponseBody.create(
                                MediaType.parse("application/json"),
                                "{}")
                        )
                    )
                )
            )

        accountPresenter.start()

        verify(accountView).showMessage(R.string.http_request_error)
    }

    @Test
    fun `should save user and display a success feedback`() {
        `when`(accountInteractor.editUser(mockedUser)).thenReturn(Completable.complete())

        accountPresenter.saveUser(mockedUser)

        verify(accountView).userUpdatedMessage()
    }

    @Test
    fun `should not save user when there is no internet connection`() {
        `when`(accountInteractor.editUser(mockedUser))
            .thenReturn(Completable.error(UnknownHostException()))

        accountPresenter.saveUser(mockedUser)

        verify(accountView, never()).saveUser()
        verify(accountView).showMessage(R.string.login_network_error)
    }

    @Test
    fun `should not save an invalid user`() {
        `when`(accountView.isValidUser()).thenReturn(false)

        accountPresenter.onClickSaveMyAccount()

        verify(accountView, never()).saveUser()
    }

    @Test
    fun shouldNavigateBack() {
        accountPresenter.onClickNavigateBack()
        verify(accountView).navigateBack()
    }

    @Test
    fun shouldSwitchAvatar() {
        accountPresenter.onClickSwitchAvatar()
        verify(accountView).navigateToSwitchAvatar()
    }

    @Test
    fun shouldShowLogoutAlert() {
        accountPresenter.onClickLogout()
        verify(accountView).logoutAlert()
    }

    @Test
    fun shouldLogoutUser() {
        accountPresenter.logout()
        verify(accountInteractor).clearAllLocalData()
        verify(accountView).navigateToMakeLogout()
    }

    @Test
    fun shouldChangeLockState() {
        accountPresenter.onClickLock()
        verify(accountView).changeLockState()
    }

    @Test
    fun shouldSetSelectedAvatar() {
        val mockDrawableId = 2
        val mockPosition = 2
        accountPresenter.setSelectedAvatar(mockDrawableId, mockPosition)

        verify(accountView).swapAvatar(mockDrawableId, mockPosition)
    }

    @Test
    fun shouldNotSetSelectedAvatar() {
        val mockDrawableInvalidId = null
        val mockDrawableValidId = 1
        val mockPosition = 2

        accountPresenter.setSelectedAvatar(mockDrawableInvalidId, mockPosition)

        verify(accountView, never()).swapAvatar(mockDrawableValidId, mockPosition)
    }

    private fun createMockUser(): User =
        User(1, "2", "testUser", "test@test.test")


}