package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.network.interceptors.AuthenticationInterceptor
import br.com.ilhasoft.voy.network.interceptors.ContentTypeHeaderInterceptor
import br.com.ilhasoft.voy.shared.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate


/**
 * Created by lucasbarros on 05/01/18.
 */
abstract class BaseFactory<out ApiType>(private var mClazz: Class<ApiType>) {

    companion object {
        var accessToken: String = ""
        var certificate: X509Certificate? = null
    }

    private var mRetrofitBuilder: Retrofit.Builder = createBaseRetrofit()

    val anonymousApi: ApiType by lazy {
        mRetrofitBuilder.client(createOkHttpClient(certificate).build()).build().create<ApiType>(mClazz)
    }

    val api: ApiType by lazy {
        val clientBuilder = createOkHttpClient(certificate)
        clientBuilder.addInterceptor(AuthenticationInterceptor())
            .addInterceptor(ContentTypeHeaderInterceptor())

        mRetrofitBuilder.client(clientBuilder.build()).build().create<ApiType>(mClazz)
    }

    val apiFile: ApiType by lazy {
        val clientBuilder = createOkHttpClient(certificate)
        clientBuilder.addInterceptor(AuthenticationInterceptor())
            .addInterceptor(ContentTypeHeaderInterceptor("multipart/form-data"))

        mRetrofitBuilder.client(clientBuilder.build()).build().create<ApiType>(mClazz)
    }

    private fun createBaseRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    abstract fun createOkHttpClient(certificate: X509Certificate?): OkHttpClient.Builder

}