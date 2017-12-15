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
        binding.presenter = presenter
    }

    override fun onBind(map: Map) {
        binding.map = map
        binding.executePendingBindings()
    }

}