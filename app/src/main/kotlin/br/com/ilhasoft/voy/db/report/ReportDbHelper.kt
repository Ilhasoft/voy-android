package br.com.ilhasoft.voy.db.report

import br.com.ilhasoft.voy.db.theme.BoundDbModel
import br.com.ilhasoft.voy.db.theme.StringDbModel
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ThemeData
import io.reactivex.Flowable
import io.reactivex.Single
import io.realm.Realm

/**
 * Created by lucasbarros on 09/02/18.
 */
class ReportDbHelper {

    private val realm by lazy { Realm.getDefaultInstance() }

    fun getReports(): Flowable<List<Report>> {
        return Flowable.fromCallable {
            val reportsDb = realm.where(ReportDbModel::class.java)
                    .equalTo(ReportDbModel::themeId.name, ThemeData.themeId).findAll()
            reportsDb.map { it.toReport() }.toMutableList()
        }
    }

    fun saveReport(theme: Int, location: Location, description: String?, name: String,
                   tags: List<String>, medias: List<String>, urls: List<String>?): Single<Report> {
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
                this.mediasPath.addAll(medias.map { path -> StringDbModel().apply { text = path } })
                urls?.let {
                    this.urls.addAll(it.map { title ->
                        StringDbModel().apply {
                            text = title
                        }
                    })
                }
            }
            realm.executeTransaction {
                val nextId = realm.where(ReportDbModel::class.java).max(ReportDbModel::internalId.name)?.toInt()
                if (nextId != null) {
                    reportDb.internalId = nextId + 1
                }
                reportDb = realm.copyToRealm(reportDb)
            }
            reportDb.toReport()
        }
    }

    fun removeReport(reportInternalId: Int) {
        realm.executeTransaction {
            val reportDb = realm.where(ReportDbModel::class.java).equalTo(ReportDbModel::internalId.name, reportInternalId).findAll()
            reportDb.deleteAllFromRealm()
        }
    }
}