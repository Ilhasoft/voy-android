package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.voy.models.User
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by erickjones on 09/02/18.
 */
interface AccountInteractor {

    fun getUser(): Single<User?>

    fun editUser(user: User): Completable

    fun clearAllLocalData()

}