package br.com.ilhasoft.voy.network.users

import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by developer on 09/01/18.
 */
class UserService : ServiceFactory<UserApi>(UserApi::class.java) {

    fun getUsers(): Flowable<MutableList<User>> = api.getUsers()

    fun getUser(userId: String): Single<User> = api.getUser(userId)

}