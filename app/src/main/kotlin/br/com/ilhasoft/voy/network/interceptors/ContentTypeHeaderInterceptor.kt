package br.com.ilhasoft.voy.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by lucasbarros on 05/01/18.
 */
class ContentTypeHeaderInterceptor(private val contentType: String = "application/json") : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val requestBuilder = original.newBuilder()
                .header("Content-Type", contentType)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}