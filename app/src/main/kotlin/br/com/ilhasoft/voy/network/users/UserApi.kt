package br.com.ilhasoft.voy.network.users

import br.com.ilhasoft.voy.models.User
import io.reactivex.Completable
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * Created by developer on 03/01/18.
 */
interface UserApi {

    @GET("/api/users/")
    fun getUser(@Query("auth_token") token: String): Flowable<List<User>>

    @PUT("/api/users/{id}/")
    fun editUser(@Path("id") userId: Int, @Body user: User): Completable

    @PUT("/api/users/{id}/")
    fun editUser(@Path("id") userId: Int, @Body avatar: String): Completable
}