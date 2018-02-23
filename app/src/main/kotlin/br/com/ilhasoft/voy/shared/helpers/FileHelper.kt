package br.com.ilhasoft.voy.shared.helpers

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import br.com.ilhasoft.support.core.helpers.IoHelper
import java.io.File

/**
 * Created by lucasbarros on 22/01/18.
 */
object FileHelper {

    val imageTypes = listOf("jpg", "jpeg", "png", "bmp")

    fun getMimeTypeFromUri(context: Context, uri: Uri) : String {
        return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
        }
    }

    fun createFileFromUri(context: Context, uri: Uri): File = File(IoHelper.getFilePathForUri(context, uri))
}