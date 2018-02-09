package br.com.ilhasoft.voy.ui.account

import android.content.Context
import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.models.SharedPreferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.users.UserService
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by erickjones on 09/02/18.
 */
class AccountInteractorImpl(val context: Context) :AccountInteractor {

    private val userService by lazy { UserService() }
    private val userPreference by lazy { SharedPreferences(context) }
    private val accountPreferencesHelper: AccountPreferencesHelper by lazy { AccountPreferencesHelper(userPreference)}

    override fun getUser(): Single<User> {
        return if (ConnectivityManager.isConnected()) {
            userService.getUser(userPreference.getInt(User.ID))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap { accountPreferencesHelper.saveUser(it)}

        } else {
            accountPreferencesHelper.getUser()
        }
    }
}