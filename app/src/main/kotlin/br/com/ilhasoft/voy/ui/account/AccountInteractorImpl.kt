package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.db.base.BaseDbHelper
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.users.UserChangeRequest
import br.com.ilhasoft.voy.network.users.UserRepository
import br.com.ilhasoft.voy.shared.extensions.extractNumbers
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by erickjones on 09/02/18.
 */
class AccountInteractorImpl(
    private val preferences: Preferences,
    private val userRepository: UserRepository,
    private val baseDbHelper: BaseDbHelper,
    private val connectionProvider: CheckConnectionProvider,
    private val scheduler: BaseScheduler
) : AccountInteractor {

    override fun getUser(): Single<User?> {
        return if (connectionProvider.hasConnection()) {
            userRepository.getUser()
                .fromIoToMainThread(scheduler)
                .flatMap { saveUser(it) }
        } else {
            getUserFromPreferences()
        }
    }

    override fun editUser(user: User): Completable {
        val requestObject = UserChangeRequest(user.id, user.avatar.extractNumbers(), user.password)
        return userRepository.editUser(requestObject)
            .fromIoToMainThread(scheduler)
            .doOnComplete { saveAvatar(user.avatar) }
    }

    override fun clearAllLocalData() {
        preferences.clear()
        baseDbHelper.deleteAllData()
    }

    private fun getUserFromPreferences(): Single<User?> {
        return Single.fromCallable {
            preferences.run {
                User(
                    getInt(User.ID),
                    getString(User.AVATAR),
                    getString(User.USERNAME),
                    getString(User.EMAIL)
                )
            }
        }
    }

    private fun saveUser(user: User): Single<User> {
        return Single.fromCallable {
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