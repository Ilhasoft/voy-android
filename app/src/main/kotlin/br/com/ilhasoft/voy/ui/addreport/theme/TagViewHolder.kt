package br.com.ilhasoft.voy.ui.addreport.theme

import android.view.View
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemTagBinding
import br.com.ilhasoft.voy.models.Tag

/**
 * Created by geral on 11/12/17.
 */
class TagViewHolder(val binding: ItemTagBinding,
                    val presenter: AddThemeFragmentPresenter) : ViewHolder<Tag>(binding.root), View.OnClickListener {

    init {
        binding.clicked = false
        binding.tag.setOnClickListener(this)
    }

    override fun onBind(tag: Tag) {
        binding.tag.text = tag.name
    }

    override fun onClick(v: View) {
        binding.clicked = binding.clicked.not()
        binding.clicked.let {
            binding.tag.isSelected = !v.isSelected
        }
    }
}