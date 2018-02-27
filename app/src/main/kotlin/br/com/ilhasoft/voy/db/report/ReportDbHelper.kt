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
        reportInternalId: Int?,
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        medias: List<String>,
        reportId: Int? = null,
        newFiles: List<String>? = null,
        filesToDelete: List<ReportFile>? = null
    ): Single<Report> {

        return Single.fromCallable {
            var reportDb = createDbModel(
                theme,
                location,
                name,
                description,
                tags,
                medias,
                urls,
                reportId,
                newFiles,
                filesToDelete
            )
            realm.executeTransaction {
                reportInternalId?.let {
                    realm.where(ReportDbModel::class.java)
                        .equalTo(ReportDbModel::internalId.name, it)
                        .findFirst()?.let { reportDb.internalId = it.internalId }
                }
                if (reportDb.internalId == 0) {
                    reportDb.internalId = autoIncrementInternalId()
                }
                reportDb = realm.copyToRealmOrUpdate(reportDb)
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

    private fun createDbModel(
        theme: Int,
        location: Location,
        name: String,
        description: String?,
        tags: List<String>,
        medias: List<String>,
        urls: List<String>?,
        reportId: Int?,
        newFiles: List<String>?,
        filesToDelete: List<ReportFile>?
    ): ReportDbModel {
        return ReportDbModel().apply {
            themeId = theme
            this.location = BoundDbModel().apply {
                lat = location.coordinates[1]
                lng = location.coordinates[0]
            }
            this.name = name
            this.description = description
            this.tags.addAll(tags)
            this.mediasPath.addAll(medias)
            urls?.let {
                this.urls.addAll(it)
            }
            reportId?.let {  id = it }
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
        }
    }

    private fun autoIncrementInternalId(): Int {
        val nextId =
            realm.where(ReportDbModel::class.java).max(ReportDbModel::internalId.name)
                ?.toInt()
        return if (nextId != null) {
            nextId + 1
        } else {
            1
        }
    }
}