package br.com.ilhasoft.voy.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by lucasbarros on 05/01/18.
 */
class AuthenticationInterceptor(private val accessToken: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val requestBuilder = original.newBuilder()
                .header("Authorization", accessToken)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}