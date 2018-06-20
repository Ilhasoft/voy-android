package br.com.ilhasoft.voy.db.theme

import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.network.themes.ThemeDataSource
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import io.reactivex.Flowable
import io.reactivex.Single
import io.realm.Realm

/**
 * Created by lucasbarros on 08/02/18.
 */
class ThemeDbHelper(private val realm: Realm, private val scheduler: BaseScheduler) : ThemeDataSource {

    override fun getThemes(project: Int?, user: Int?): Flowable<List<Theme>> {
        return Flowable.just(project)
            .onMainThread(scheduler)
            .flatMap {
                Flowable.just(
                    realm.where(ThemeDbModel::class.java)
                        .equalTo("projectId", project)
                        .findAll()
                )
            }.map { it.map { it.toTheme() } }
    }

    override fun getTheme(themeId: Int, project: Int?, yearStart: Int?, yearEnd: Int?, user: Int?): Single<Theme> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveThemes(themes: List<Theme>): Flowable<MutableList<Theme>> {
        val themesDb = themes.map { createThemeDb(it) }
        return Flowable.fromIterable(themesDb)
            .onMainThread(scheduler)
            .doOnNext { dbModel ->
                realm.executeTransaction {
                    it.copyToRealmOrUpdate(dbModel)
                }
            }
            .toList()
            .flatMapPublisher { Flowable.just(it.map { it.toTheme() }.toMutableList()) }
    }

    private fun createThemeDb(theme: Theme): ThemeDbModel = ThemeDbModel().apply {
        id = theme.id
        projectId = theme.project.id
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
        startAt = theme.startAt
        endAt = theme.endAt
    }

    fun getThemeTags(themeId: Int): Flowable<MutableList<String>> {
        return Flowable.fromCallable {
            val theme = realm.where(ThemeDbModel::class.java).equalTo("id", themeId).findFirst()
            theme?.tags?.toMutableList() ?: mutableListOf()
        }
    }
}