package br.com.ilhasoft.voy.network.users

import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.regex.Pattern

/**
 * Created by developer on 09/01/18.
 */
class UserService : ServiceFactory<UserApi>(UserApi::class.java) {

    fun getUsers(): Flowable<MutableList<User>> = api.getUsers()

    fun getUser(userId: Int): Single<User> = api.getUser(userId)

    fun getUser(): Flowable<List<User>> = api.getUser(accessToken)

    fun editUser(user: User): Completable {
        val newUser = processUserAvatarToUpdate(user)
        return api.editUser(user.id, newUser)
    }

    private fun processUserAvatarToUpdate(user: User): User {
        val pattern = Pattern.compile("\\d+")
        val matcher = pattern.matcher(user.avatar)

        return if (matcher.find()) {
            user.copy(avatar = matcher.group())
        } else {
            user
        }
    }

}