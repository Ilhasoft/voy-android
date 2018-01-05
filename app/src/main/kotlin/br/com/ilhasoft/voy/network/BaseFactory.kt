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
abstract class BaseFactory<out ApiType>(private var mClazz: Class<ApiType>,
                                        private var accessToken: String = "") {

    private var mRetrofitBuilder: Retrofit.Builder

    private var mApi: ApiType? = null

    init {
        mRetrofitBuilder = createBaseRetrofit()
    }

    private fun createBaseRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    fun getApi(): ApiType? {
        if (mApi == null) {
            val clientBuilder = createOkHttpClient()
            clientBuilder.addInterceptor(AuthenticationInterceptor(accessToken))

            mApi = mRetrofitBuilder.client(clientBuilder.build()).build().create<ApiType>(mClazz)
        }
        return mApi
    }

    abstract fun createOkHttpClient(): OkHttpClient.Builder


}