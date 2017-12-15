package br.com.ilhasoft.voy.ui.addreport.theme

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemThemeBinding
import br.com.ilhasoft.voy.models.Theme

/**
 * Created by geral on 12/12/17.
 */
class ThemeViewHolder(val binding: ItemThemeBinding,
                      val presenter: AddThemeFragmentPresenter) : ViewHolder<Theme>(binding.root) {

    init {
        binding.run {
            isSelected = false
            presenter = this@ThemeViewHolder.presenter
        }
    }

    override fun onBind(theme: Theme) {
        configThemeSelection(theme)
        binding.theme = theme
        binding.title.text = theme.name
    }

    fun configThemeSelection(theme: Theme?) {
        binding.isSelected = presenter.getSelectedTheme() == theme
    }

}