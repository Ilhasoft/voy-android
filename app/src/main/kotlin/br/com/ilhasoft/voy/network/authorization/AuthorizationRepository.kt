package br.com.ilhasoft.voy.network.authorization

import br.com.ilhasoft.voy.models.Credentials
import io.reactivex.Flowable

/**
 * Created by developer on 06/03/18.
 */
class AuthorizationRepository(private val remoteAuthorizationDataSource: AuthorizationDataSource)
    : AuthorizationDataSource {

    override fun loginWithCredentials(credentials: Credentials): Flowable<AuthorizationResponse> =
            remoteAuthorizationDataSource.loginWithCredentials(credentials)

}