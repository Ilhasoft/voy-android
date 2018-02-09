package br.com.ilhasoft.voy.db.report

import br.com.ilhasoft.voy.db.theme.BoundDbModel
import br.com.ilhasoft.voy.db.theme.StringDbModel
import br.com.ilhasoft.voy.db.theme.ThemeDbModel
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import io.reactivex.Flowable
import io.reactivex.Single
import io.realm.Realm
import io.realm.RealmList

/**
 * Created by lucasbarros on 09/02/18.
 */
class ReportDbHelper {

    private val realm by lazy { Realm.getDefaultInstance() }

    fun getReports(): Flowable<List<Report>> {
        return Flowable.fromCallable {
            val reportsDb = realm.where(ReportDbModel::class.java).equalTo("id", 0).findAll()
            reportsDb.map { it.toTheme() }.toMutableList()
        }
    }

    fun saveReport(theme: Int,
                   location: Location,
                   description: String?,
                   name: String,
                   tags: List<String>,
                   urls: List<String>?): Single<Report>{
        //TODO: Refactor to include realm with RxJava
        return Single.fromCallable {
            var reportDb = ReportDbModel().apply {
                themeId = theme
                this.location = BoundDbModel().apply {
                    lat = location.coordinates[1]
                    lng = location.coordinates[0]
                }
                this.name = name
                this.description = description
                this.tags.addAll(tags.map { title -> StringDbModel().apply { text = title } })
                urls?.let {
                    this.urls.addAll(it.map { title ->
                        StringDbModel().apply {
                            text = title
                        }
                    })
                }
            }
            realm.beginTransaction()
            reportDb = realm.copyToRealmOrUpdate(reportDb)
            realm.commitTransaction()
            reportDb.toReport()
        }
    }
}