package br.com.ilhasoft.voy.ui.report.detail.holder

import android.view.View
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ViewIndicatorBinding
import br.com.ilhasoft.voy.models.Indicator
import br.com.ilhasoft.voy.ui.report.detail.ReportDetailPresenter

/**
 * Created by jones on 12/27/17.
 */
class IndicatorViewHolder(val binding: ViewIndicatorBinding,
                          val presenter: ReportDetailPresenter): ViewHolder<Indicator>(binding.root),
View.OnClickListener {

    init {
        binding.presenter = presenter
        binding.itemIndicator.setOnClickListener(this)
    }

    override fun onBind(indicator: Indicator) {
        binding.indicator = indicator
    }

    override fun onClick(v: View) {
        println("CLICKED ${v.isSelected}")
    }
}