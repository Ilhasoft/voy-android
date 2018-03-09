package br.com.ilhasoft.voy.network.themes

import br.com.ilhasoft.voy.models.Theme
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by erickjones on 01/03/18.
 */

class ThemeRepository(private val remoteThemeDataSource: ThemeDataSource): ThemeDataSource {

    override fun getThemes(project: Int?, user: Int?): Flowable<MutableList<Theme>> =
            remoteThemeDataSource.getThemes(project, user)

    override fun getTheme(themeId: Int, project: Int?, yearStart: Int?, yearEnd: Int?, user: Int?): Single<Theme> =
            remoteThemeDataSource.getTheme(themeId, project, yearStart, yearEnd, user)

}