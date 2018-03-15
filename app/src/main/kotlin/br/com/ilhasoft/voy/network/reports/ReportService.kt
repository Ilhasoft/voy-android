package br.com.ilhasoft.voy.network.reports

import android.util.Log
import android.webkit.MimeTypeMap
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.network.ServiceFactory
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.putIfNotNull
import br.com.ilhasoft.voy.shared.helpers.RetrofitHelper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.RequestBody
import java.io.File

/**
 * Created by lucasbarros on 08/01/18.
 */
class ReportService : ServiceFactory<ReportsApi>(ReportsApi::class.java), ReportDataSource {

    override fun getReports(theme: Int? , project: Int?, mapper: Int?, status: Int?): Single<List<Report>> {
        return api.getReports(createReportQuery(theme, project, mapper, status))
            .map { it.results }
    }

    override fun saveReport(report: Report): Single<Report> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getReports(
        page: Int? = null,
        page_size: Int? = null,
        theme: Int? = null,
        project: Int? = null,
        mapper: Int? = null,
        status: Int? = null
    ): Single<Response<Report>> {

        val reportsRequest = mutableMapOf<String, Int?>()
        reportsRequest.apply {
            putIfNotNull("page", page)
            putIfNotNull("page_size", page_size)
            putIfNotNull("theme", theme)
            putIfNotNull("project", project)
            putIfNotNull("mapper", mapper)
            putIfNotNull("status", status)
        }
        return api.getReports(reportsRequest)
    }

    private fun createReportQuery(theme: Int? = null, project: Int? = null, mapper: Int? = null,
                                  status: Int? = null): Map<String, Int?> {
        return mutableMapOf<String, Int?>().apply {
            putIfNotNull("page", 1)
            putIfNotNull("page_size", 50)
            putIfNotNull("theme", theme)
            putIfNotNull("project", project)
            putIfNotNull("mapper", mapper)
            putIfNotNull("status", status)
        }
    }

    override fun getReport(
        id: Int,
        theme: Int?,
        project: Int?,
        mapper: Int?,
        status: Int?
    ): Single<Report> {

        val reportsRequest = mutableMapOf<String, Int?>()
        reportsRequest.apply {
            putIfNotNull("theme", theme)
            putIfNotNull("project", project)
            putIfNotNull("mapper", mapper)
            putIfNotNull("status", status)
        }
        return api.getReport(id, reportsRequest)
    }

    override fun saveReport(
        theme: Int, location: Location, description: String?, name: String,
        tags: List<String>, urls: List<String>?, medias: List<File>
    ): Observable<Report> {
        var auxReport = Report()
        return saveReportInternal(theme, location, description, name, tags, urls)
            .flatMapObservable {
                auxReport = it
                Observable.fromIterable(medias)
            }
            .flatMapSingle { saveFile(it, auxReport.id) }
            .doOnNext {
                auxReport.files.add(it)
                if (it.mediaType == "image")
                    auxReport.lastImage = it
            }
            .toList()
            .flatMapObservable { Observable.just(auxReport) }
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
        filesToDelete: List<Int>?
    ): Observable<Report> {
        return if (filesToDelete?.isNotEmpty() == true) {
            deleteFilesAndUpdateReport(
                reportId,
                theme,
                location,
                description,
                name,
                tags,
                urls,
                newFiles,
                filesToDelete
            )
        } else {
            updateReportInternal(
                reportId,
                theme,
                location,
                description,
                name,
                tags,
                urls,
                newFiles
            )
        }
    }

    private fun deleteFilesAndUpdateReport(
        reportId: Int,
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        newFiles: List<File>? = null,
        filesToDelete: List<Int>?
    ): Observable<Report> {
        return Observable.fromIterable(filesToDelete)
            .flatMapCompletable {
                deleteFile(it)
            }
            .toSingleDefault(true)
            .flatMapObservable {
                updateReportInternal(
                    reportId,
                    theme,
                    location,
                    description,
                    name,
                    tags,
                    urls,
                    newFiles
                )
            }
    }

    private fun updateReportInternal(
        reportId: Int,
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        newFiles: List<File>? = null
    ): Observable<Report> {

        val requestBody = ReportRequest(theme, location, description, name, tags, urls)

        return if (newFiles?.isNotEmpty() == true) {
            var auxReport = Report()
            api.updateReport(reportId, requestBody).flatMapObservable {
                auxReport = it
                Observable.fromIterable(newFiles)
            }
                .flatMapSingle { saveFile(it, auxReport.id) }
                .doOnNext {
                    auxReport.files.add(it)
                    if (it.mediaType == "image")
                        auxReport.lastImage = it
                }
                .toList()
                .flatMapObservable { Observable.just(auxReport) }
        } else {
            api.updateReport(reportId, requestBody).toObservable()
        }

    }

    private fun deleteFile(fileId: Int): Completable = api.deleteFile(fileId)

    private fun saveReportInternal(
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?
    ): Single<Report> {
        val request = ReportRequest(theme, location, description, name, tags, urls)
        return api.saveReport(request)
    }

    private fun saveFile(
        title: String,
        description: String,
        file: File,
        mimeType: String,
        report: Int,
        mediaType: String = ""
    ): Single<ReportFile> {

        val requestMap = mutableMapOf<String, RequestBody>()
        requestMap.apply {
            putIfNotNull("title", RetrofitHelper.createPartFromString(title))
            putIfNotNull("description", RetrofitHelper.createPartFromString(description))
            putIfNotNull("media_type", RetrofitHelper.createPartFromString(mediaType))
            putIfNotNull("report_id", RetrofitHelper.createPartFromString(report.toString()))
        }

        val requestFile = RetrofitHelper.prepareFilePart("file", file, mimeType)
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        val fileType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)

        if (fileType.contains("video")) {
            val requestVideoThumbnail = RetrofitHelper.prepareVideoThumbnail("thumbnail", file)
            return apiFile.saveFile(requestMap, requestFile, requestVideoThumbnail)
        }
        return apiFile.saveFile(requestMap, requestFile)
    }

    override fun saveFile(file: File, reportId: Int): Single<ReportFile> {
        //TODO: try to send others files if one fail
        return saveFile(file.nameWithoutExtension, file.name, file, "", reportId)
            .fromIoToMainThread()
    }

}
