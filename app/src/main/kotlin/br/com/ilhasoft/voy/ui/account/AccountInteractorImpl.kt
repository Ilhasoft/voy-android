package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.users.UserChangeRequest
import br.com.ilhasoft.voy.network.users.UserService
import br.com.ilhasoft.voy.shared.extensions.extractNumbers
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by erickjones on 09/02/18.
 */
class AccountInteractorImpl(val preferences: Preferences) : AccountInteractor {

    private val userService by lazy { UserService() }

    override fun getUser(): Flowable<User?> {
        return if (ConnectivityManager.isConnected()) {
            userService.getUser()
                    .fromIoToMainThread()
                    .flatMap { saveUser(it) }

        } else {
            getUserFromPreferences()
        }
    }

    override fun editUser(user: User): Completable {
        val requestObject = UserChangeRequest(user.id, user.avatar.extractNumbers(), user.password)
        return userService.editUser(requestObject)
                .fromIoToMainThread()
                .doOnComplete { saveAvatar(user.avatar) }
    }

    override fun removeUserPreferencesEntries() {
        preferences.clear()
    }

    private fun getUserFromPreferences(): Flowable<User?> {
        return Flowable.fromCallable {
            preferences.run {
                User(getInt(User.ID),
                        getString(User.AVATAR),
                        getString(User.USERNAME),
                        getString(User.EMAIL))
            }

        }
    }

    private fun saveUser(user: User): Flowable<User> {
        return Flowable.fromCallable {
            preferences.run {
                put(User.ID, user.id)
                put(User.USERNAME, user.username)
                put(User.EMAIL, user.email)
                put(User.AVATAR, user.avatar)
            }
            user
        }
    }

    private fun saveAvatar(avatar: String) = preferences.put(User.AVATAR, avatar)

}