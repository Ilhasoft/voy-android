package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * Created by lucasbarros on 05/01/18.
 */
abstract class ServiceFactory<out ApiType>(clazz: Class<ApiType>) :
    BaseFactory<ApiType>(clazz) {

    override fun createOkHttpClient(): OkHttpClient.Builder {
        val client = OkHttpClient.Builder()
            .writeTimeout(4, TimeUnit.MINUTES)
            .readTimeout(4, TimeUnit.MINUTES)
            .connectTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(loggingInterceptor)
        }
        return client
    }

}
