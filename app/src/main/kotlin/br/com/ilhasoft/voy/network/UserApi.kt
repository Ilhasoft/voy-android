package br.com.ilhasoft.voy.network

import android.content.Context
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.shared.Constants
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by developer on 03/01/18.
 */
interface UserApi {

    @FormUrlEncoded
    @GET("/api/users/")
    fun getUsers(): Flowable<List<User>>

    @FormUrlEncoded
    @GET("/api/users/{id}/")
    fun getUser(@Path("id") userId: String): Flowable<User>

}