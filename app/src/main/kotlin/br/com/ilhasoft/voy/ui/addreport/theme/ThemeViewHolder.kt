package br.com.ilhasoft.voy.ui.addreport.theme

import android.view.View
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemThemeBinding
import br.com.ilhasoft.voy.models.Theme

/**
 * Created by geral on 12/12/17.
 */
class ThemeViewHolder(val binding: ItemThemeBinding,
                      val presenter: AddThemeFragmentPresenter) : ViewHolder<Theme>(binding.root), View.OnClickListener {

    init {
        binding.option.setOnClickListener(this)
    }

    override fun onBind(theme: Theme) {
        binding.title.text = theme.name
    }

    override fun onClick(v: View?) {

    }
}