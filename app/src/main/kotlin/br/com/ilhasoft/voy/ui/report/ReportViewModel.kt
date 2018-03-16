package br.com.ilhasoft.voy.ui.report

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.ilhasoft.voy.models.Report

/**
 * Created by felipe on 05/03/18.
 */
class ReportViewModel : ViewModel() {

    private var pendingReports: MutableLiveData<List<Report>> = MutableLiveData()
    private var approvedReports: MutableLiveData<List<Report>> = MutableLiveData()
    private var unApprovedReports: MutableLiveData<List<Report>> = MutableLiveData()

    var onDemandStatus = true

    fun notifyReports(reports: List<Report>, status: ReportStatus) {
        getReportsFromStatus(status).value = reports
    }

    fun getReports(status: ReportStatus): LiveData<List<Report>> = getReportsFromStatus(status)

    private fun getReportsFromStatus(status: ReportStatus) = when(status) {
        ReportStatus.APPROVED -> approvedReports
        ReportStatus.PENDING -> pendingReports
        ReportStatus.UNAPPROVED -> unApprovedReports
    }

    fun notifyOnDemand(next: String?) {
        if(next == "")
            onDemandStatus = false
    }
}