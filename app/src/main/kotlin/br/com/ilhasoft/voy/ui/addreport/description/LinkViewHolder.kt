package br.com.ilhasoft.voy.ui.addreport.description

import android.view.View
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemLinkBinding
import br.com.ilhasoft.voy.ui.addreport.ReportViewModel

/**
 * Created by geral on 11/12/17.
 */
class LinkViewHolder(val binding: ItemLinkBinding, reportViewModel: ReportViewModel) :
        ViewHolder<String>(binding.root) {

    init {
        binding.reportViewModel = reportViewModel
    }

    override fun onBind(link: String) {
        binding.link = link
        binding.executePendingBindings()
    }

}