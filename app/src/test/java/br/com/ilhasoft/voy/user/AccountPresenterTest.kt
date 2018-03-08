package br.com.ilhasoft.voy.user

import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.ui.account.AccountContract
import br.com.ilhasoft.voy.ui.account.AccountInteractor
import br.com.ilhasoft.voy.ui.account.AccountPresenter
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
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
        `when`(accountInteractor.getUser()).thenReturn(Flowable.just(mockedUser))

        accountPresenter.start()

        verify(accountView).setUser(mockedUser)
    }

    @Test
    fun shouldSaveUserAndDisplayFeedback() {
        `when`(accountInteractor.editUser(mockedUser)).thenReturn(Completable.complete())

        accountPresenter.saveUser(mockedUser)

        verify(accountView).userUpdatedMessage()
    }

    @Test
    fun shouldNotSaveAndDisplayUser() {
        `when`(accountInteractor.editUser(mockedUser))
                .thenReturn(Completable.error(UnknownHostException()))

        accountPresenter.saveUser(mockedUser)

        verify(accountView).showMessage(R.string.login_network_error)
    }

    @Test
    fun shouldNotSaveInvalidUser() {
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
    fun shouldLogoutUser() {
        accountPresenter.onClickLogout()
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