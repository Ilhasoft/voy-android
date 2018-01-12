package br.com.ilhasoft.voy.network.authorization

import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by lucasbarros on 05/01/18.
 */
class AuthorizationService : ServiceFactory<AuthorizationApi>(AuthorizationApi::class.java) {

    fun loginWithCredentials(credentials: Credentials): Flowable<AuthorizationResponse> {
        return anonymousApi.loginWithCredentials(credentials)
    }
}