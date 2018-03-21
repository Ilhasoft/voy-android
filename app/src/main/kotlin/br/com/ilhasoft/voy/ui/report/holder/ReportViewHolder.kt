package br.com.ilhasoft.voy.ui.report.holder

import android.support.v4.content.ContextCompat
import android.widget.PopupMenu
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ItemReportBinding
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.ui.report.ReportStatus
import br.com.ilhasoft.voy.ui.report.fragment.ReportPresenter

/**
 * Created by developer on 01/12/17.
 */
class ReportViewHolder(
    val binding: ItemReportBinding,
    val presenter: ReportPresenter
) : ViewHolder<Report>(binding.root) {

    private lateinit var popupMenu: PopupMenu

    init {
        binding.presenter = presenter
    }

    override fun onBind(report: Report) {
        binding.let {
            it.themeIndicator.setBackgroundColor(ThemeData.themeColor)
            it.pendingToSend = (report.status == ReportStatus.PENDING.value) && report.shouldSend
            it.report = report

            it.executePendingBindings()
        }
    }
}