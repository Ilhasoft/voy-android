package br.com.ilhasoft.voy.ui.addreport.theme

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.FragmentAddThemeBinding
import br.com.ilhasoft.voy.databinding.ItemTagBinding
import br.com.ilhasoft.voy.ui.addreport.theme.AddThemeFragmentPresenter

/**
 * Created by geral on 11/12/17.
 */
class TagViewHolder(val binding: ItemTagBinding,
                    val presenter: AddThemeFragmentPresenter) : ViewHolder<String>(binding.root) {

    override fun onBind(tag: String?) {
        binding.tag.text = tag
    }
}