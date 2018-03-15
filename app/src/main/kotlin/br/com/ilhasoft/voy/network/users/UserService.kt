package br.com.ilhasoft.voy.network.users

import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by developer on 09/01/18.
 */
class UserService : ServiceFactory<UserApi>(UserApi::class.java), UserDataSource {

    override fun getUser(): Single<User?> = api.getUser().map {
        if(it.isNotEmpty())
            it[0]
        else
            null
    }

    override fun editUser(userRequest: UserChangeRequest): Completable =
        api.editUser(userRequest.id, userRequest)

}