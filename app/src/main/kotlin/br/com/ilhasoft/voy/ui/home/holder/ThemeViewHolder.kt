package br.com.ilhasoft.voy.ui.home.holder

import android.graphics.Color
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ItemThemeBinding
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.ui.home.HomePresenter

/**
 * Created by geral on 12/12/17.
 */
class ThemeViewHolder(val binding: ItemThemeBinding,
                      val presenter: HomePresenter) : ViewHolder<Theme>(binding.root) {

    init {
        binding.presenter = presenter
    }

    override fun onBind(theme: Theme) {
        binding.theme = theme
        binding.cardView.setCardBackgroundColor(Color.parseColor(getString(R.string.color_hex,
                theme.color)))
        binding.executePendingBindings()
    }

}