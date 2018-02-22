package br.com.ilhasoft.voy.ui.shared

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemTagBinding
import br.com.ilhasoft.voy.models.TagDataUI
import br.com.ilhasoft.voy.ui.addreport.ReportViewModel

/**
 * Created by erickjones on 06/02/18.
 */
class TagViewHolder(val binding: ItemTagBinding,
                    tagDataUI: TagDataUI,
                    val reportViewModel: ReportViewModel?) : ViewHolder<String>(binding.root) {

    init {
        binding.tagDataUI = tagDataUI
        reportViewModel?.apply {
            binding.root.setOnClickListener {
                if(binding.selected)
                    removeTag(binding.tag!!)
                else
                    addTag(binding.tag!!)
                binding.selected = binding.selected.not()
            }
        }
    }

    override fun onBind(tag: String) {
        binding.tag = tag
        binding.selected = if (reportViewModel != null) reportViewModel.isTagSelected(tag) else true
        binding.executePendingBindings()
    }

}