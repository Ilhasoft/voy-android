package br.com.ilhasoft.voy.db.report

import br.com.ilhasoft.voy.db.theme.BoundDbModel
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.network.reports.ReportDataSource
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import br.com.ilhasoft.voy.ui.report.ReportStatus
import io.reactivex.Flowable
import io.reactivex.Single
import io.realm.Realm

/**
 * Created by lucasbarros on 09/02/18.
 */
class ReportDbHelper(private val realm: Realm) : ReportDataSource {

    override fun getReports(theme: Int? ,project: Int?, mapper: Int?, status: Int?): Single<List<Report>> {
        return Single.fromCallable {
            val reportsDb = realm.where(ReportDbModel::class.java)
                .equalTo(ReportDbModel::themeId.name, theme).findAll()
            reportsDb.map { it.toReport() }.toList()
        }.onMainThread()
    }

    override fun saveReport(report: Report): Single<Report> {
        return saveReport(report.internalId, theme = report.theme, location = report.location!!,
            description = report.description, name = report.name, tags = report.tags, urls = report.urls,
            medias = report.files.map { it.file }, reportId = report.id, status = report.status,
            shouldSend = report.shouldSend
        ).onMainThread()
    }

    fun getReportDbModels(): Flowable<List<ReportDbModel>> {
        return Flowable.fromCallable {
            val reportsDb = realm.where(ReportDbModel::class.java)
                .equalTo(ReportDbModel::themeId.name, ThemeData.themeId).findAll()
            reportsDb.toMutableList()
        }
    }

    fun saveReport(
        reportInternalId: String? = null,
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        medias: List<String>,
        reportId: Int? = null,
        newFiles: List<String>? = null,
        filesToDelete: List<ReportFile>? = null,
        status: Int = ReportStatus.PENDING.value,
        shouldSend: Boolean = true
    ): Single<Report> {

        return Single.fromCallable {
            var reportDb = getReport(reportId ?: 0)
            if (reportDb == null) {
                reportDb = createDbModel(
                    theme,
                    location,
                    name,
                    description,
                    tags,
                    medias,
                    urls,
                    reportId,
                    newFiles,
                    filesToDelete,
                    status,
                    shouldSend
                )
            }

            realm.executeTransaction { transaction ->
                reportDb?.let { transaction.copyToRealmOrUpdate(it) }
            }
            reportDb.toReport()
        }
    }

    fun removeReport(reportInternalId: String) {
        realm.executeTransaction {
            val reportDb = realm.where(ReportDbModel::class.java)
                .equalTo(ReportDbModel::internalId.name, reportInternalId).findAll()
            reportDb.deleteAllFromRealm()
        }
    }

    private fun getReport(id: Int): ReportDbModel? {
        return realm.where(ReportDbModel::class.java).equalTo("id", id).findFirst()
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
        filesToDelete: List<ReportFile>?,
        status: Int = ReportStatus.PENDING.value,
        shouldSend: Boolean = true
    ): ReportDbModel {
        return ReportDbModel().apply {
            themeId = theme
            this.location = BoundDbModel().apply {
                lat = location.coordinates[1]
                lng = location.coordinates[0]
            }
            this.name = name
            this.status = status
            this.description = description
            this.tags.addAll(tags)
            this.mediasPath.addAll(medias)
            this.shouldSend = shouldSend
            urls?.let {
                this.urls.addAll(it)
            }
            reportId?.let { id = it }
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
}