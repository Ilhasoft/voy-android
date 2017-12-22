package br.com.ilhasoft.voy.ui.addreport.description

import android.view.View
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemLinkBinding

/**
 * Created by geral on 11/12/17.
 */
class LinkViewHolder(val binding: ItemLinkBinding,
                     val presenter: AddDescriptionFragmentPresenter) : ViewHolder<String>(binding.root), View.OnClickListener {

    init {
        binding.presenter = presenter
        binding.remove.setOnClickListener(this)
    }

    override fun onBind(link: String) {
        binding.link = link
        binding.executePendingBindings()
    }


    override fun onClick(v: View?) {
        binding.link?.let{
            presenter.removeLink(it)
        }
    }
}