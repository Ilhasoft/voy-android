package br.com.ilhasoft.voy.ui.report.detail.holder

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemReportDetailLinkBinding
import br.com.ilhasoft.voy.ui.report.detail.ReportDetailPresenter

/**
 * Created by allan on 12/04/18.
 */
class LinkViewHolder(
        val binding: ItemReportDetailLinkBinding,
        val presenter: ReportDetailPresenter
) :
        ViewHolder<String>(binding.root) {

    init {
        binding.presenter = presenter
    }

    override fun onBind(link: String) {
        binding.link = link
        binding.executePendingBindings()
    }

}