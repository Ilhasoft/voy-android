package br.com.ilhasoft.voy.network.medias

import br.com.ilhasoft.voy.models.ReportMedia
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lucasbarros on 11/01/18.
 */
interface MediasApi {

    @GET("/api/report-medias/")
    fun getMedias(@Query("report") id: Int): Flowable<List<ReportMedia>>

}