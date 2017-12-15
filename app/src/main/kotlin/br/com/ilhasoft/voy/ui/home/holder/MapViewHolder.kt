package br.com.ilhasoft.voy.ui.home.holder

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemMapBinding
import br.com.ilhasoft.voy.models.Map
import br.com.ilhasoft.voy.ui.home.HomePresenter

/**
 * Created by developer on 05/12/17.
 */
class MapViewHolder(val binding: ItemMapBinding,
                    val presenter: HomePresenter) : ViewHolder<Map>(binding.root) {

    init {
        binding.run {
            isSelected = false
            presenter = this@MapViewHolder.presenter
        }
    }

    override fun onBind(map: Map) {
        configMapSelection(map)
        binding.map = map
        binding.executePendingBindings()
    }

    private fun configMapSelection(map: Map) {
        binding.isSelected = presenter.getSelectedMap() == map
    }

}