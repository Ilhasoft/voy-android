package br.com.ilhasoft.voy.network.themes

import br.com.ilhasoft.voy.models.Theme
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Created by developer on 09/01/18.
 */
interface ThemeApi {

    @GET("/api/themes/")
    fun getThemes(@QueryMap parameters: Map<String, Int?>): Flowable<MutableList<Theme>>

    @GET("/api/themes/{id}/")
    fun getTheme(@Path("id") themeId: Int,
                 @QueryMap parameters: Map<String, Int?>): Single<Theme>

}