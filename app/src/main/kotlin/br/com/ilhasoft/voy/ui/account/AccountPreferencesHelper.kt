package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.voy.models.SharedPreferences
import br.com.ilhasoft.voy.models.User
import io.reactivex.Single

/**
 * Created by erickjones on 09/02/18.
 */
class AccountPreferencesHelper (private val userPreferences: SharedPreferences) {

    fun getUser() : Single<User> {
        return Single.fromCallable {
            val allEntries = userPreferences.getAllEntries()
            allEntries.map { User() }
        }
    }

    fun saveUser(user: User) :Single<User> {

    }

}