package br.com.ilhasoft.voy.network.projects

import br.com.ilhasoft.voy.models.Project
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by developer on 09/01/18.
 */
interface ProjectApi {

    @GET("/api/projects/")
    fun getProjects(@Query("lang") lang: String): Flowable<MutableList<Project>>

    @GET("/api/projects/{id}/")
    fun getProject(@Path("id") projectId: Int, @Query("lang") lang: String): Single<Project>

}