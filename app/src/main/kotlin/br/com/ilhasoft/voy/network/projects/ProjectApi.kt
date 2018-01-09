package br.com.ilhasoft.voy.network.projects

import br.com.ilhasoft.voy.models.Project
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by developer on 09/01/18.
 */
interface ProjectApi {

    @GET("/api/projects/")
    fun getProjects(): Flowable<MutableList<Project>>

    @GET("/api/projects/{id}/")
    fun getProject(@Path("id") projectId: String): Single<Project>

}