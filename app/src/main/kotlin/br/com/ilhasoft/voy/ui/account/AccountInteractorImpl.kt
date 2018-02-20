package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.users.UserService
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
        val requestObject = user.password?.let {

        }
        return userService.editUser(user)
                .fromIoToMainThread()
                .doOnComplete { saveUser(user) }
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
}