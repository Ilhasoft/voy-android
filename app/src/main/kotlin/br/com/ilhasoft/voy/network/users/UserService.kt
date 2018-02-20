package br.com.ilhasoft.voy.network.users

import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.ServiceFactory
import br.com.ilhasoft.voy.shared.extensions.extractNumbers
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by developer on 09/01/18.
 */
class UserService : ServiceFactory<UserApi>(UserApi::class.java) {

    fun getUser(): Flowable<User?> = api.getUser(accessToken).map {
        if(it.isNotEmpty())
            it[0]
        else
            null
    }

    fun editUser(user: User):  Completable {
        return api.editUser(user.id, user)
    }


}