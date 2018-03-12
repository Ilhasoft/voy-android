package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.db.base.BaseDbHelper
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.users.UserChangeRequest
import br.com.ilhasoft.voy.network.users.UserDataSource
import br.com.ilhasoft.voy.network.users.UserRepository
import br.com.ilhasoft.voy.shared.extensions.extractNumbers
import br.com.ilhasoft.voy.shared.schedulers.ImmediateScheduler
import io.reactivex.Completable
import io.reactivex.Flowable
import io.realm.Realm
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.lang.NullPointerException

/**
 * Created by felipe on 12/03/18.
 */
class AccountInteractorImplTest {

    private lateinit var accountInteractor: AccountInteractorImpl
    private lateinit var userRepository: UserRepository
    @Mock
    private lateinit var userDataSource: UserDataSource
    @Mock
    private lateinit var preferences: Preferences
    @Mock
    private lateinit var baseDbHelper: BaseDbHelper
    @Mock
    private lateinit var connectionProvider: CheckConnectionProvider
    private val mockedUser = User(1, "avatar5", "username", "email")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        userRepository = UserRepository(userDataSource)
        accountInteractor = AccountInteractorImpl(
            preferences, userRepository,
            baseDbHelper, connectionProvider,
            ImmediateScheduler()
        )
    }

    @Test
    fun `Should get user from service when online`() {
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(userDataSource.getUser()).thenReturn(Flowable.just(mockedUser))

        accountInteractor.getUser()
            .test()
            .assertComplete()
            .assertValue(mockedUser)

        verify(preferences).put(User.ID, mockedUser.id)
        verify(preferences).put(User.USERNAME, mockedUser.username)
        verify(preferences).put(User.EMAIL, mockedUser.email)
        verify(preferences).put(User.AVATAR, mockedUser.avatar)
    }

    @Test
    fun `Should get user from preferences when offline`() {
        `when`(connectionProvider.hasConnection()).thenReturn(false)
        `when`(preferences.getInt(User.ID)).thenReturn(1)
        `when`(preferences.getString(User.USERNAME)).thenReturn("username")
        `when`(preferences.getString(User.EMAIL)).thenReturn("email")
        `when`(preferences.getString(User.AVATAR)).thenReturn("avatar5")

        accountInteractor.getUser()
            .test()
            .assertComplete()
            .assertValue(mockedUser)
    }

    @Test
    fun `Should throw NPE when not find user access token`() {
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(userDataSource.getUser()).thenReturn(Flowable.error(NullPointerException()))

        accountInteractor.getUser()
            .test()
            .assertError(NullPointerException::class.java)
    }

    @Test
    fun `Should update user when call service`() {
        mockedUser.password = "password"
        val requestObject = UserChangeRequest(mockedUser.id,
            mockedUser.avatar.extractNumbers(), mockedUser.password)
        `when`(userDataSource.editUser(requestObject)).thenReturn(Completable.complete())

        accountInteractor.editUser(mockedUser)
            .test()
            .assertComplete()

        verify(preferences).put(User.AVATAR, mockedUser.avatar)
    }

    @Test
    fun `Should thorw error when call update`() {
        mockedUser.password = "password"
        val requestObject = UserChangeRequest(mockedUser.id,
            mockedUser.avatar.extractNumbers(), mockedUser.password)
        // Uses generic exception because the layers bellow is responsible to treat the errors better
        `when`(userDataSource.editUser(requestObject)).thenReturn(Completable.error(Exception()))

        accountInteractor.editUser(mockedUser)
            .test()
            .assertError(Exception::class.java)
    }

    @Test
    fun `Should logout and clear all data`() {
        accountInteractor.clearAllLocalData()

        verify(preferences).clear()
        verify(baseDbHelper).deleteAllData()
    }
}