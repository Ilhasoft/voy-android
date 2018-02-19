package br.com.ilhasoft.voy.ui.report.holder

import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.widget.ImageView
import android.widget.PopupMenu
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ItemReportBinding
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.ui.report.fragment.ReportFragment
import br.com.ilhasoft.voy.ui.report.fragment.ReportPresenter

/**
 * Created by developer on 01/12/17.
 */
class ReportViewHolder(val binding: ItemReportBinding,
                       val presenter: ReportPresenter) : ViewHolder<Report>(binding.root) {

    private lateinit var popupMenu: PopupMenu

    init {
        binding.presenter = presenter
    }

    override fun onBind(report: Report) {
        binding.let {
            it.themeIndicator.setBackgroundColor(ThemeData.themeColor)
            it.pending = report.status == ReportFragment.PENDING_STATUS
            it.report = report

            it.expandedMenu.run {
                if (report.internalId != null) {
                    setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_status_resend))
                    setupPopupMenu(this)
                    this.setOnClickListener { popupMenu.show() }
                } else {
                    setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_status_waiting))
                    this.setOnClickListener(null)
                }
            }
            it.executePendingBindings()
        }
    }

    private fun setupPopupMenu(expandedMenu: ImageView) {
        popupMenu = PopupMenu(context, expandedMenu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.resend -> {
                    presenter.onClickResend(binding.report!!)
                    true
                }
                else -> false
            }
        }
        popupMenu.menuInflater.inflate(R.menu.resend_report, popupMenu.menu)
    }

}