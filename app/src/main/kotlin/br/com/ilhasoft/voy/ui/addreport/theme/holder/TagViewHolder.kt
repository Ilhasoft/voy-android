package br.com.ilhasoft.voy.ui.addreport.theme.holder

import android.view.View
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemTagThemeBinding
import br.com.ilhasoft.voy.models.Tag
import br.com.ilhasoft.voy.ui.addreport.theme.AddThemeFragmentPresenter

/**
 * Created by geral on 11/12/17.
 */
class TagViewHolder(val binding: ItemTagThemeBinding,
                    val presenter: AddThemeFragmentPresenter) : ViewHolder<Tag>(binding.root), View.OnClickListener {

    init {
        binding.clicked = false
        binding.tagTitle.setOnClickListener(this)
    }

    override fun onBind(tag: Tag) {
        binding.tag = tag
    }

    override fun onClick(v: View) {
        binding.clicked = binding.clicked?.not()
        binding.clicked.let {
            binding.tagTitle.isSelected = !v.isSelected
        }
    }
}