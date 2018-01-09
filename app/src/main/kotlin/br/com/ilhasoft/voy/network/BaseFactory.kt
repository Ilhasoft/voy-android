package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.network.interceptors.AuthenticationInterceptor
import br.com.ilhasoft.voy.shared.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by lucasbarros on 05/01/18.
 */
abstract class BaseFactory<out ApiType>(private var mClazz: Class<ApiType>) {

    companion object {
        var accessToken: String = ""
    }

    private var mRetrofitBuilder: Retrofit.Builder = createBaseRetrofit()

    val anonymousApi: ApiType by lazy {  mRetrofitBuilder.client(createOkHttpClient().build()).build().create<ApiType>(mClazz) }

    val api: ApiType by lazy {
        val clientBuilder = createOkHttpClient()
        clientBuilder.addInterceptor(AuthenticationInterceptor())

        mRetrofitBuilder.client(clientBuilder.build()).build().create<ApiType>(mClazz)
    }

    private fun createBaseRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    abstract fun createOkHttpClient(): OkHttpClient.Builder


}