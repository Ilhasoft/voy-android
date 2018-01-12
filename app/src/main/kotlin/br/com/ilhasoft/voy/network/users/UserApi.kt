package br.com.ilhasoft.voy.network.users

import br.com.ilhasoft.voy.models.User
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by developer on 03/01/18.
 */
interface UserApi {

    @GET("/api/users/")
    fun getUsers(): Flowable<MutableList<User>>

    @GET("/api/users/{id}/")
    fun getUser(@Path("id") userId: Int): Single<User>

    @GET("/api/users/")
    fun getUser(@Query("auth_token") token: String): Flowable<List<User>>

}