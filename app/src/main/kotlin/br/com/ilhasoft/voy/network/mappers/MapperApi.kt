package br.com.ilhasoft.voy.network.mappers

import br.com.ilhasoft.voy.models.Mapper
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Created by developer on 09/01/18.
 */
interface MapperApi {

    @GET("/api/mappers/")
    fun getMappers(@QueryMap parameters: Map<String, Int?>): Flowable<MutableList<Mapper>>

    @GET("/api/mappers/{id}/")
    fun getMapper(@Path("id") mapperId: String,
                  @QueryMap parameters: Map<String, Int?>): Single<Mapper>

}