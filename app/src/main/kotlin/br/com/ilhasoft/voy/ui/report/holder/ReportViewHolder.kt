package br.com.ilhasoft.voy.ui.report.holder

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemReportBinding
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.report.ReportsPresenter

/**
 * Created by developer on 01/12/17.
 */
class ReportViewHolder(val binding: ItemReportBinding,
                       val presenter: ReportsPresenter) : ViewHolder<Report>(binding.root) {

    init {
        binding.presenter = presenter
    }

    override fun onBind(report: Report) {
        binding.report = report
        binding.executePendingBindings()
    }

}