package br.com.ilhasoft.voy.ui.addreport.theme.holder

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemThemeBinding
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.ui.addreport.theme.AddThemeFragmentPresenter

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
    }

    private fun configThemeSelection(theme: Theme?) {
        binding.isSelected = presenter.getSelectedTheme() == theme?.id
    }

}