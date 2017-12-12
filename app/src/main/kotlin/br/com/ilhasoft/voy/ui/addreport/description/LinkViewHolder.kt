package br.com.ilhasoft.voy.ui.addreport.description

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemLinkBinding

/**
 * Created by geral on 11/12/17.
 */
class LinkViewHolder(val binding: ItemLinkBinding,
                     val presenter: AddDescriptionFragmentPresenter) : ViewHolder<String>(binding.root) {

    override fun onBind(link: String?) {
        link?.let {
            binding.link.text = it
        }
    }
}