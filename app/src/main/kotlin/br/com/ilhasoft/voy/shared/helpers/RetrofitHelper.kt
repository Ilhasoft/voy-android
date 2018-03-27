package br.com.ilhasoft.voy.shared.helpers

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.provider.MediaStore
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
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

    fun prepareVideoThumbnail(partName: String, file: File): MultipartBody.Part {
        val thumbnailBitmap = ThumbnailUtils.createVideoThumbnail(file.absolutePath, MediaStore.Images.Thumbnails.MINI_KIND)
        val outputStream = ByteArrayOutputStream()
        thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        val requestFile = RequestBody.create(MediaType.parse("image/png"), outputStream.toByteArray())
        return MultipartBody.Part.createFormData(partName, "${file.name}_thumbnail.png", requestFile)
    }

}