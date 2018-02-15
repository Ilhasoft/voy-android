package br.com.ilhasoft.voy.ui.account

import android.content.Context
import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.models.SharedPreferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.users.UserService
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by erickjones on 09/02/18.
 */
class AccountInteractorImpl(val context: Context) : AccountInteractor {

    private val userService by lazy { UserService() }
    private val userPreferences by lazy { SharedPreferences(context) }

    override fun getUser(): Flowable<List<User>> {
        return if (ConnectivityManager.isConnected()) {
            userService.getUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap { saveUser(it.first()) }

        } else {
            getUserFromPreferences()
        }
    }

    override fun removeUserPreferencesEntries() {
        userPreferences.apply {
            remove(User.ID)
            remove(User.TOKEN)
            remove(User.AVATAR)
            remove(User.USERNAME)
            remove(User.EMAIL)
        }
    }

    private fun getUserFromPreferences(): Flowable<List<User>> {
        return Flowable.fromCallable {
            mutableListOf(User.createUserFromPreferencesMap(userPreferences.getAllEntries()))
        }
    }

    private fun saveUser(user: User): Flowable<List<User>> {
        return Flowable.fromCallable {
            userPreferences.apply {
                put(User.ID, user.id)
                put(User.USERNAME, user.username)
                put(User.EMAIL, user.email)
                put(User.AVATAR, user.avatar)
            }
            mutableListOf(user)
        }
    }
}