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

    override fun getThemes(project: Int?, user: Int?): Flowable<List<Theme>> {
        return if (connectionProvider.hasConnection()) {
            remoteThemeDataSource.getThemes(project, user)
        } else {
            localThemeDataSource.getThemes(project, user)
        }
    }

    override fun getTheme(
        themeId: Int,
        project: Int?,
        yearStart: Int?,
        yearEnd: Int?,
        user: Int?
    ): Single<Theme> =
        remoteThemeDataSource.getTheme(themeId, project, yearStart, yearEnd, user)

    override fun saveThemes(themes: List<Theme>): Flowable<MutableList<Theme>> {
        return if (connectionProvider.hasConnection())
            localThemeDataSource.saveThemes(themes)
        else
            Flowable.just(themes.toMutableList())
    }
}