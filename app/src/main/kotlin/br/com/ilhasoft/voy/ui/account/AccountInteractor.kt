package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.voy.models.User
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by erickjones on 09/02/18.
 */
interface AccountInteractor {

    fun getUser(): Flowable<User?>

    fun editUser(user: User): Completable

    fun editAvatar(user: User): Completable

    fun removeUserPreferencesEntries()

}