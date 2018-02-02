package br.com.ilhasoft.voy.network.notification

import br.com.ilhasoft.voy.models.Notification
import io.reactivex.Flowable
import retrofit2.http.GET

/**
 * Created by lucasbarros on 02/02/18.
 */
interface NotificationApi {

    @GET("/api/report-notification/")
    fun getNotifications(): Flowable<List<Notification>>

}