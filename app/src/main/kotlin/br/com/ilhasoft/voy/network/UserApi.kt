package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.models.User
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by developer on 03/01/18.
 */
interface UserApi {

    @GET("/api/users/")
    fun getUsers(): Flowable<List<User>>

    @GET("/api/users/{id}/")
    fun getUser(@Path("id") userId: String): Single<User>

}