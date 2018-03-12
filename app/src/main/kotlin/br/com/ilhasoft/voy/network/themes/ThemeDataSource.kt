package br.com.ilhasoft.voy.network.themes

import br.com.ilhasoft.voy.models.Theme
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by erickjones on 01/03/18.
 */
interface ThemeDataSource {

    fun getThemes(project: Int? = null, user: Int? = null): Flowable<List<Theme>>

    fun getTheme(
        themeId: Int,
        project: Int? = null,
        yearStart: Int? = null,
        yearEnd: Int? = null,
        user: Int? = null
    ): Single<Theme>

    fun saveThemes(themes: List<Theme>): Flowable<MutableList<Theme>>
}