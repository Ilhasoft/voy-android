package br.com.ilhasoft.voy.ui.addreport.tag

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemTagThemeBinding
import br.com.ilhasoft.voy.models.Tag
import br.com.ilhasoft.voy.ui.addreport.ReportViewModel

/**
 * Created by geral on 11/12/17.
 */
class TagViewHolder(val binding: ItemTagThemeBinding,
                    val reportViewModel: ReportViewModel) : ViewHolder<Tag>(binding.root) {

    init {
        binding.root.setOnClickListener {
            if (binding.selected)
                reportViewModel.removeTag(binding.tag!!)
            else
                reportViewModel.addTag(binding.tag!!)

            binding.selected = binding.selected.not()
        }
    }

    override fun onBind(tag: Tag) {
        binding.tag = tag
        binding.selected = reportViewModel.isTagSelected(tag)
        binding.executePendingBindings()
    }

}