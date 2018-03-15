package br.com.ilhasoft.voy.network.users

import br.com.ilhasoft.voy.models.User
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by developer on 03/01/18.
 */
interface UserApi {

    // The `me` query is not required
    @GET("/api/users/")
    fun getUser(@Query("me") userId: Int = 1): Single<List<User>>

    @PUT("/api/users/{id}/")
    fun editUser(@Path("id") userId: Int, @Body user: UserChangeRequest): Completable

}