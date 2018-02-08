package br.com.ilhasoft.voy.network.themes

import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.network.ServiceFactory
import br.com.ilhasoft.voy.shared.extensions.putIfNotNull
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by developer on 09/01/18.
 */
class ThemeService : ServiceFactory<ThemeApi>(ThemeApi::class.java) {

    fun getThemes(project: Int? = null,
                  user: Int? = null): Flowable<MutableList<Theme>> {
        val themesRequest = mutableMapOf<String, Int?>()
        themesRequest.apply {
            putIfNotNull("project", project)
            putIfNotNull("user", user)
        }
        return api.getThemes(themesRequest)
    }

    fun getTheme(themeId: Int,
                 project: Int? = null,
                 yearStart: Int? = null,
                 yearEnd: Int? = null,
                 user: Int? = null): Single<Theme> {
        val themesRequest = mutableMapOf<String, Int?>()
        themesRequest.apply {
            putIfNotNull("project", project)
            putIfNotNull("year_start", yearStart)
            putIfNotNull("year_end", yearEnd)
            putIfNotNull("user", user)
        }
        return api.getTheme(themeId, themesRequest)
    }

}