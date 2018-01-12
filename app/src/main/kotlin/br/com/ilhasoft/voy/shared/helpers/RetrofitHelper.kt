package br.com.ilhasoft.voy.shared.helpers

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by lucasbarros on 12/01/18.
 */
object RetrofitHelper {

    fun createPartFromString(plainText: String?): RequestBody? {
        return if (plainText != null) RequestBody.create(MediaType.parse("text/plain"), plainText) else null
    }

    fun prepareFilePart(partName: String, file: File, mimeType: String): MultipartBody.Part {
        val requestFile = RequestBody.create(MediaType.parse(mimeType), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

}