package br.com.ilhasoft.voy.ui.addreport

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.net.Uri
import br.com.ilhasoft.voy.models.AddReportFragmentType

/**
 * Created by lucasbarros on 15/01/18.
 */
class ReportViewModel : ViewModel() {

    var nextEnable: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var title: String? = null
    var medias: MutableList<Uri> = mutableListOf()

    fun addUri(uri: Uri) {
        medias.add(uri)
        isNextEnable(AddReportFragmentType.MEDIAS)
    }

    fun removeUri(uri: Uri) {
        medias.apply {
            remove(single { it == uri })
        }
        isNextEnable(AddReportFragmentType.MEDIAS)
    }

    fun isNextEnable(type: AddReportFragmentType) {
        when (type) {
            AddReportFragmentType.MEDIAS -> nextEnable.value = medias.isNotEmpty()
            else -> nextEnable.value = false
        }

    }
}