package br.com.ilhasoft.voy.ui.report.holder

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ItemReportBinding
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.report.fragment.ReportFragment
import br.com.ilhasoft.voy.ui.report.fragment.ReportPresenter

/**
 * Created by developer on 01/12/17.
 */
class ReportViewHolder(val binding: ItemReportBinding,
                       val presenter: ReportPresenter) : ViewHolder<Report>(binding.root),
        View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var popupMenu: PopupMenu

    init {
        binding.presenter = presenter
    }

    override fun onBind(report: Report) {
        binding.let {
            it.themeIndicator.setBackgroundColor(Color.parseColor(getString(R.string.color_hex,
                    report.themeColor)))
            it.approved = report.status == ReportFragment.APPROVED_STATUS
            it.report = report

            //TODO Pending status menu icon will be handled locally, then show correct menu icon
            it.expandedMenu.run {
                if (report.status == ReportFragment.NOT_APPROVED_STATUS)
                    setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_status_resend))
                setupPopupMenu(this)
                this.setOnClickListener(this@ReportViewHolder)
            }
            it.executePendingBindings()
        }
    }

    override fun onClick(v: View?) {
        popupMenu.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.resend -> {
            presenter.onClickEditReport(binding.report)
            true
        }
        else -> false
    }

    private fun setupPopupMenu(expandedMenu: ImageView) {
        popupMenu = PopupMenu(context, expandedMenu)
        popupMenu.setOnMenuItemClickListener(this)

        //TODO Pending status menu will be handled locally, then show correct popup menu
        when (binding.report?.status) {
            ReportFragment.NOT_APPROVED_STATUS -> {
                popupMenu.menuInflater.inflate(R.menu.resend_report, popupMenu.menu)
            }
        }
    }

}