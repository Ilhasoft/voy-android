package br.com.ilhasoft.voy.network.users

import br.com.ilhasoft.voy.models.User
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by erickjones on 01/03/18.
 */
class UserRepository(private val remoteUserDataSource: UserDataSource) : UserDataSource {

    override fun getUser(): Single<User?> =
        remoteUserDataSource.getUser()

    override fun editUser(userRequest: UserChangeRequest): Completable =
        remoteUserDataSource.editUser(userRequest)

}