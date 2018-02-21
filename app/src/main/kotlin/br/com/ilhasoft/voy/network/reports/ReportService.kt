package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.network.ServiceFactory
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.putIfNotNull
import br.com.ilhasoft.voy.shared.helpers.RetrofitHelper
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.RequestBody
import java.io.File

/**
 * Created by lucasbarros on 08/01/18.
 */
class ReportService : ServiceFactory<ReportsApi>(ReportsApi::class.java) {

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

    fun getReport(
        id: Int,
        theme: Int? = null,
        project: Int? = null,
        mapper: Int? = null,
        status: Int? = null
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

    fun saveReport(
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

    fun updateReport(
        reportId: Int,
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?
    ): Single<Report> {

        val requestBody = ReportRequest(theme, location, description, name, tags, urls)

        return api.updateReport(reportId, requestBody)
    }

    fun updateReport(
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

        return apiFile.saveFile(requestMap, requestFile)
    }

    fun saveFile(file: File, reportId: Int): Single<ReportFile> {
        //TODO: try to send others files if one fail
        return saveFile(file.nameWithoutExtension, file.name, file, "", reportId)
            .fromIoToMainThread()
    }

}
