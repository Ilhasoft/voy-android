package br.com.ilhasoft.voy.ui.report.detail.holder

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemTagBinding
import br.com.ilhasoft.voy.models.Tag

/**
 * Created by developer on 12/12/17.
 */
class TagViewHolder(val binding: ItemTagBinding) : ViewHolder<Tag>(binding.root) {

    override fun onBind(tag: Tag?) {
        binding.tag = tag
        binding.executePendingBindings()
    }
}