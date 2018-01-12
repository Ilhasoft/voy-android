package br.com.ilhasoft.voy.network.files

import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.network.ServiceFactory
import br.com.ilhasoft.voy.network.reports.Response
import br.com.ilhasoft.voy.shared.extensions.putIfNotNull
import br.com.ilhasoft.voy.shared.helpers.RetrofitHelper
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.RequestBody
import java.io.File

/**
 * Created by lucasbarros on 11/01/18.
 */
class FilesService : ServiceFactory<FilesApi>(FilesApi::class.java) {

    fun getFiles(page: Int? = null,
                 page_size: Int? = null,
                 theme: Int? = null,
                 report: Int? = null,
                 project: Int? = null,
                 media_type: String? = null): Single<Response<ReportFile>> {

        val request = mutableMapOf<String, Any?>()
        request.apply {
            putIfNotNull("page", page)
            putIfNotNull("page_size", page_size)
            putIfNotNull("theme", theme)
            putIfNotNull("report", report)
            putIfNotNull("project", project)
            putIfNotNull("media_type", media_type)
        }

        return api.getFiles(request)
    }

    fun saveFile(title: String,
                 description: String,
                 media_type: String,
                 file: File,
                 mimeType: String,
                 report: Int): Single<ReportFile> {

        val requestMap = mutableMapOf<String, RequestBody>()
        requestMap.apply {
            putIfNotNull("title", RetrofitHelper.createPartFromString(title))
            putIfNotNull("description", RetrofitHelper.createPartFromString(description))
            putIfNotNull("media_type", RetrofitHelper.createPartFromString(media_type))
            putIfNotNull("report_id", RetrofitHelper.createPartFromString(report.toString()))
        }

        val requestFile = RetrofitHelper.prepareFilePart("file", file, mimeType)

        return apiFile.saveFile(requestMap, requestFile)
    }

    fun deleteFile(fileId: Int,
                   theme: Int? = null,
                   report: Int? = null,
                   project: Int? = null,
                   media_type: String? = null): Single<Void> {

        val requestMap = mutableMapOf<String, Any?>()
        requestMap.apply {
            putIfNotNull("theme", theme)
            putIfNotNull("report", report)
            putIfNotNull("project", project)
            putIfNotNull("media_type", media_type)
        }

        return api.deleteFile(fileId, requestMap)
    }

}