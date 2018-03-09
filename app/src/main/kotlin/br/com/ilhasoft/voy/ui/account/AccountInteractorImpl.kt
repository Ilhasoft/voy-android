package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.users.UserChangeRequest
import br.com.ilhasoft.voy.network.users.UserDataSource
import br.com.ilhasoft.voy.network.users.UserRepository
import br.com.ilhasoft.voy.network.users.UserService
import br.com.ilhasoft.voy.shared.extensions.extractNumbers
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import io.reactivex.Completable
import io.reactivex.Flowable
import io.realm.Realm

/**
 * Created by erickjones on 09/02/18.
 */
class AccountInteractorImpl(
    private val preferences: Preferences,
    val userRepository: UserRepository,
    private val realm: Realm
) : AccountInteractor {

    override fun getUser(): Flowable<User?> {
        return if (ConnectivityManager.isConnected()) {
            userRepository.getUser()
                    .fromIoToMainThread()
                    .flatMap { saveUser(it) }

        } else {
            getUserFromPreferences()
        }
    }

    override fun editUser(user: User): Completable {
        val requestObject = UserChangeRequest(user.id, user.avatar.extractNumbers(), user.password)
        return userRepository.editUser(requestObject)
                .fromIoToMainThread()
                .doOnComplete { saveAvatar(user.avatar) }
    }

    override fun clearAllLocalData() {
        preferences.clear()
        realm.executeTransaction {
            it.deleteAll()
        }
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