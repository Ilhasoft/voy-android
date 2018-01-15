package br.com.ilhasoft.voy.network.authorization

import br.com.ilhasoft.voy.models.Credentials
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by lucasbarros on 05/01/18.
 */
interface AuthorizationApi {

    @POST("get_auth_token/")
    fun loginWithCredentials(@Body body: Credentials): Flowable<AuthorizationResponse>
}