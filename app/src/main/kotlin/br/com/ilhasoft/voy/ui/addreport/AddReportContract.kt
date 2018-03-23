package br.com.ilhasoft.voy.ui.addreport

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.AddReportFragmentType
import java.io.File

/**
 * Created by lucasbarros on 23/11/17.
 */
interface AddReportContract : BasicView {
    fun navigateBack()
    fun navigateToThanks()
    fun navigateToNext(type: AddReportFragmentType)
    fun getVisibleFragmentType(): AddReportFragmentType
    fun getFileFromUri(uri: Uri): File
    fun getMimeTypeFromUri(uri: Uri): String
    fun checkLocation()
    fun isUpdate(): Boolean
    fun dismissLoadLocationDialog()
    fun stopGettingLocation()
    fun showOutsideDialog()
}