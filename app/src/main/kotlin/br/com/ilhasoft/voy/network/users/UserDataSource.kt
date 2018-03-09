package br.com.ilhasoft.voy.network.users

import br.com.ilhasoft.voy.models.User
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by erickjones on 01/03/18.
 */
interface UserDataSource {
    fun getUser(): Flowable<User?>
    fun editUser(userRequest: UserChangeRequest): Completable
}