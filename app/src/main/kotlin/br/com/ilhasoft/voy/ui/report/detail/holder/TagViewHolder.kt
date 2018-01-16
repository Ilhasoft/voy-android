package br.com.ilhasoft.voy.ui.report.detail.holder

import android.graphics.Color
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ItemTagBinding
import br.com.ilhasoft.voy.ui.report.detail.ReportDetailPresenter

/**
 * Created by developer on 12/12/17.
 */
class TagViewHolder(val binding: ItemTagBinding,
                    val presenter: ReportDetailPresenter) : ViewHolder<String>(binding.root) {

    override fun onBind(tag: String?) {
        binding.cardView.setCardBackgroundColor(Color.parseColor(getString(R.string.color_hex,
                presenter.getThemeColor())))
        binding.tag = tag
        binding.executePendingBindings()
    }
}