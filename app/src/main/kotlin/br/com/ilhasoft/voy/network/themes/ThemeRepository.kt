package br.com.ilhasoft.voy.network.themes

import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.models.Theme
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by erickjones on 01/03/18.
 */
class ThemeRepository(
    private val remoteThemeDataSource: ThemeDataSource,
    private val localThemeDataSource: ThemeDataSource,
    private val connectionProvider: CheckConnectionProvider
) : ThemeDataSource {

    override fun getThemes(project: Int?, user: Int?, lang: String): Flowable<List<Theme>> {
        return if (connectionProvider.hasConnection()) {
            remoteThemeDataSource.getThemes(project, user, lang)
        } else {
            localThemeDataSource.getThemes(project, user, lang)
        }
    }

    override fun getTheme(
        themeId: Int,
        project: Int?,
        yearStart: Int?,
        yearEnd: Int?,
        user: Int?,
        lang: String
    ): Single<Theme> =
        remoteThemeDataSource.getTheme(themeId, project, yearStart, yearEnd, user, lang)

    override fun saveThemes(themes: List<Theme>): Flowable<MutableList<Theme>> {
        return if (connectionProvider.hasConnection())
            localThemeDataSource.saveThemes(themes)
        else
            Flowable.just(themes.toMutableList())
    }
}