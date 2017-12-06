package br.com.ilhasoft.voy.ui.home.holder

import android.view.View
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemMapBinding
import br.com.ilhasoft.voy.models.Map
import br.com.ilhasoft.voy.ui.home.HomePresenter

/**
 * Created by developer on 05/12/17.
 */
class MapViewHolder(val binding: ItemMapBinding,
                    val presenter: HomePresenter) :
        ViewHolder<Map>(binding.root), View.OnClickListener {

    init {
        binding.isSelected = false
        binding.mapView.setOnClickListener(this)
    }

    override fun onBind(map: Map) {
        binding.map = map
        binding.executePendingBindings()
    }

    override fun onClick(v: View?) {
        binding.isSelected = binding.isSelected?.not()
    }

}