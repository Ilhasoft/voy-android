package br.com.ilhasoft.voy.db.report

import br.com.ilhasoft.voy.db.theme.BoundDbModel
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
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

    fun getReportDbModels(): Flowable<List<ReportDbModel>> {
        return Flowable.fromCallable {
            val reportsDb = realm.where(ReportDbModel::class.java)
                .equalTo(ReportDbModel::themeId.name, ThemeData.themeId).findAll()
            reportsDb.toMutableList()
        }
    }

    fun saveReport(
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        medias: List<String>,
        urls: List<String>?
    ): Single<Report> {
        return Single.fromCallable {
            var reportDb = ReportDbModel().apply {
                themeId = theme
                this.location = BoundDbModel().apply {
                    lat = location.coordinates[1]
                    lng = location.coordinates[0]
                }
                this.name = name
                this.description = description
                this.tags.addAll(tags)
                this.mediasPath.addAll(medias)
                this.newFiles.addAll(medias)
                urls?.let {
                    this.urls.addAll(it)
                }
            }
            realm.executeTransaction {
                val nextId =
                    realm.where(ReportDbModel::class.java).max(ReportDbModel::internalId.name)
                        ?.toInt()
                if (nextId != null) {
                    reportDb.internalId = nextId + 1
                }
                reportDb = realm.copyToRealm(reportDb)
            }
            reportDb.toReport()
        }
    }

    fun saveReport(
        reportId: Int,
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        medias: List<String>,
        newFiles: List<String>?,
        filesToDelete: List<ReportFile>?
    ): Single<Report> {
        return Single.fromCallable {
            var reportDb = ReportDbModel().apply {
                id = reportId
                themeId = theme
                this.location = BoundDbModel().apply {
                    lat = location.coordinates[1]
                    lng = location.coordinates[0]
                }
                this.name = name
                this.description = description
                this.tags.addAll(tags)
                this.mediasPath.addAll(medias)
                newFiles?.let {
                    this.newFiles.addAll(it)
                }
                filesToDelete?.let {
                    this.filesToDelete.addAll(filesToDelete.map { reportFile ->
                        mediasPath.remove(reportFile.file)
                        ReportFileDbModel().apply {
                            id = reportFile.id
                            file = reportFile.file
                        }
                    })
                }
                urls?.let {
                    this.urls.addAll(it)
                }
            }
            realm.executeTransaction {
                val nextId =
                    realm.where(ReportDbModel::class.java).max(ReportDbModel::internalId.name)
                        ?.toInt()
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
            val reportDb = realm.where(ReportDbModel::class.java)
                .equalTo(ReportDbModel::internalId.name, reportInternalId).findAll()
            reportDb.deleteAllFromRealm()
        }
    }
}