package br.com.ilhasoft.voy.network.themes

import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.network.ServiceFactory
import br.com.ilhasoft.voy.shared.extensions.putIfNotNull
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

/**
 * Created by developer on 09/01/18.
 */
class ThemeService : ServiceFactory<ThemeApi>(ThemeApi::class.java), ThemeDataSource {

    override fun getThemes(project: Int?,
                           user: Int?): Flowable<List<Theme>> {
        val themesRequest = mutableMapOf<String, Int?>()
        themesRequest.apply {
            putIfNotNull("project", project)
            putIfNotNull("user", user)
        }
        return api.getThemes(themesRequest, Locale.getDefault().language)
    }

    override fun getTheme(themeId: Int,
                          project: Int?,
                          yearStart: Int?,
                          yearEnd: Int?,
                          user: Int?): Single<Theme> {
        val themesRequest = mutableMapOf<String, Int?>()
        themesRequest.apply {
            putIfNotNull("project", project)
            putIfNotNull("year_start", yearStart)
            putIfNotNull("year_end", yearEnd)
            putIfNotNull("user", user)
        }
        return api.getTheme(themeId, themesRequest, Locale.getDefault().language)
    }

    override fun saveThemes(themes: List<Theme>): Flowable<MutableList<Theme>> {
        return Flowable.just(themes.toMutableList())
    }
}