package br.com.ilhasoft.voy.db.theme

import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.network.themes.ThemeDataSource
import br.com.ilhasoft.voy.shared.extensions.extractNumbers
import io.reactivex.Flowable
import io.reactivex.Single
import io.realm.Realm

/**
 * Created by lucasbarros on 08/02/18.
 */
class ThemeDbHelper : ThemeDataSource {

    override fun getThemes(project: Int?, user: Int?): Flowable<List<Theme>> {
        // TODO work with realm on correct thread
        return Flowable.just(listOf())
    }

    override fun getTheme(themeId: Int, project: Int?, yearStart: Int?, yearEnd: Int?, user: Int?): Single<Theme> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveThemes(themes: List<Theme>): Flowable<MutableList<Theme>> {
        val themesDb = themes.map { createThemeDb(it) }
        val realm = getRealm()
        val persistedThemes = mutableListOf<Theme>()

        themesDb.forEach { dbModel ->
            realm.executeTransaction {
                it.copyToRealmOrUpdate(dbModel)
            }

            persistedThemes.add(dbModel.toTheme())
        }

        return Flowable.just(persistedThemes)
    }

    private fun createThemeDb(theme: Theme): ThemeDbModel = ThemeDbModel().apply {
        id = theme.id
        projectId = theme.project.extractNumbers().toInt()
        name = theme.name
        bounds.addAll(theme.bounds.map { bound ->
            BoundDbModel().apply {
                lat = bound[0]
                lng = bound[1]
            }
        })
        tags.addAll(theme.tags)
        color = theme.color
        allowLinks = theme.allowLinks
    }

    fun getThemeTags(themeId: Int): Flowable<MutableList<String>> {
        val realm = getRealm()
        return Flowable.fromCallable {
            val theme = realm.where(ThemeDbModel::class.java).equalTo("id", themeId).findFirst()
            theme?.tags?.toMutableList() ?: mutableListOf()
        }
    }

    private fun getRealm(): Realm {
        return Realm.getDefaultInstance()
    }
}