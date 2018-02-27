package br.com.ilhasoft.voy.ui.addreport

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider


/**
 * Created by lucasbarros on 09/02/18.
 */

class ReportViewModelFactory(private val interactor: AddReportInteractor) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            return ReportViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}