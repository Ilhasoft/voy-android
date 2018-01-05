package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.models.User
import io.reactivex.Flowable
import retrofit2.http.POST

/**
 * Created by lucasbarros on 05/01/18.
 */
interface AuthorizationApi {

    @POST("get_auth_token/")
    fun loginWithCredentials(): Flowable<String>
}