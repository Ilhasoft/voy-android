package br.com.ilhasoft.voy.network.tags

import br.com.ilhasoft.voy.models.Tag
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Created by developer on 09/01/18.
 */
interface TagApi {

    @GET("/api/tags/")
    fun getTags(@QueryMap parameters: Map<String, Int>): Flowable<MutableList<Tag>>

    @GET("/api/tags/{id}/")
    fun getTag(@Path("id") tagId: Int): Single<Tag>

}