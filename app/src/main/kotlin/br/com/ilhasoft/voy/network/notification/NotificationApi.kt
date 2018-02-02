package br.com.ilhasoft.voy.network.notification

import br.com.ilhasoft.voy.models.Notification
import io.reactivex.Completable
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Created by lucasbarros on 02/02/18.
 */
interface NotificationApi {

    @GET("/api/report-notification/")
    fun getNotifications(): Flowable<List<Notification>>

    @PUT("/api/report-notification/{id}/")
    fun markAsRead(@Path("id") id: Int): Completable

}