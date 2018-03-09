package br.com.ilhasoft.voy.network.users

import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by developer on 09/01/18.
 */
class UserService : ServiceFactory<UserApi>(UserApi::class.java), UserDataSource {

    override fun getUser(): Flowable<User?> = api.getUser(accessToken).map {
        if(it.isNotEmpty())
            it[0]
        else
            null
    }

    override fun editUser(userRequest: UserChangeRequest): Completable =
        api.editUser(userRequest.id, userRequest)

}