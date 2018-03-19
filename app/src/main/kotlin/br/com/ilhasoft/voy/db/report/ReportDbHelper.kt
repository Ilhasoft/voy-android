package br.com.ilhasoft.voy.db.report

import br.com.ilhasoft.voy.db.theme.BoundDbModel
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.network.reports.ReportDataSource
import br.com.ilhasoft.voy.shared.extensions.format
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import br.com.ilhasoft.voy.ui.report.ReportStatus
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import java.io.File

/**
 * Created by lucasbarros on 09/02/18.
 */
class ReportDbHelper(private val realm: Realm, private val scheduler: BaseScheduler) : ReportDataSource {

    override fun getReport(
        id: Int,
        theme: Int?,
        project: Int?,
        mapper: Int?,
        status: Int?
    ): Single<Report> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveReport(
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        medias: List<File>,
        thumbnail: String
    ): Observable<Report> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveReports(reports: List<Report>): Single<List<Report>> {
        return Flowable.just(reports)
            .onMainThread(scheduler)
            .flatMap { Flowable.fromIterable(it) }
            .map { it.copy(shouldSend = false) }
            .flatMapSingle { saveReport(it) }
            .toList()
    }

    override fun updateReport(
        reportId: Int,
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        newFiles: List<File>?,
        filesToDelete: List<Int>?,
        thumbnail: String
    ): Observable<Report> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveFile(file: File, reportId: Int): Single<ReportFile> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getReports(theme: Int? ,project: Int?, mapper: Int?, status: Int?, page: Int?, page_size: Int?): Single<Pair<String?, List<Report>>> {
        val next: String? = ""
        return Single.fromCallable {
            val reportsDb = realm.where(ReportDbModel::class.java)
                .equalTo(ReportDbModel::themeId.name, theme).findAll()
            reportsDb.map { it.toReport() }.toList()
        }
            .map { next to it }
            .onMainThread(scheduler)
    }

    override fun getReports(url: String): Single<Pair<String?, List<Report>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveReport(report: Report): Single<Report> {
        return saveReport(report.internalId, theme = report.theme, location = report.location!!,
            description = report.description, name = report.name, tags = report.tags, urls = report.urls,
            medias = createReportFileDbModel(report.files), reportId = report.id, status = report.status,
            shouldSend = report.shouldSend, createdOn = report.createdOn.format("dd/MM/yyyy HH:mm"),
            thumbnail = report.thumbnail
        ).onMainThread(scheduler)
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
        medias: MutableList<ReportFileDbModel>,
        reportId: Int? = null,
        newFiles: List<String>? = null,
        filesToDelete: List<ReportFile>? = null,
        status: Int = ReportStatus.PENDING.value,
        shouldSend: Boolean = true,
        createdOn: String,
        thumbnail: String
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
                    shouldSend,
                    createdOn,
                    thumbnail
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
        medias: MutableList<ReportFileDbModel>,
        urls: List<String>?,
        reportId: Int?,
        newFiles: List<String>?,
        filesToDelete: List<ReportFile>?,
        status: Int = ReportStatus.PENDING.value,
        shouldSend: Boolean = true,
        createdOn: String,
        thumbnail: String
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
            this.medias.addAll(medias)
            this.shouldSend = shouldSend
            urls?.let {
                this.urls.addAll(it)
            }
            reportId?.let { id = it }
            newFiles?.let {
                this.newFiles.addAll(it)
            }
            filesToDelete?.map { reportFile ->
                medias.filter { it.id == reportFile.id }.map {
                    ReportFileDbModel().apply {
                        id = it.id
                        file = it.file
                    }
                }
            }
            this.createdOn = createdOn
            this.thumbnail = thumbnail
        }
    }

    private fun createReportFileDbModel(files: MutableList<ReportFile>): MutableList<ReportFileDbModel> {
        return files.map {
            ReportFileDbModel().apply {
                id = it.id
                file = it.file
            }
        }.toMutableList()

    }

}