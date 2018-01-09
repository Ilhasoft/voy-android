package br.com.ilhasoft.voy.network.reports

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by lucasbarros on 08/01/18.
 */
interface ReportsApi {

    @GET("/api/reports/")
    fun getReports(@QueryMap body: Map<String, Int?>) : Flowable<ReportsListResponse>
}