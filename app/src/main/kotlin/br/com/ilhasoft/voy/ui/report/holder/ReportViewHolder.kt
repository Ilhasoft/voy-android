package br.com.ilhasoft.voy.ui.report.holder

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
                       val presenter: ReportPresenter, status: Int) : ViewHolder<Report>(binding.root),
        View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var popupMenu: PopupMenu

    init {
        binding.presenter = presenter
        binding.pending = status == ReportFragment.PENDING_STATUS
    }

    override fun onBind(report: Report) {
        binding.report = report
        binding.expandedMenu.run {
            setupPopupMenu(this)
            this.setOnClickListener(this@ReportViewHolder)
        }
        binding.executePendingBindings()
    }

    override fun onClick(v: View?) {
        popupMenu.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.edit -> {
            true
        }
        R.id.share -> {
            true
        }
        else -> false
    }

    private fun setupPopupMenu(expandedMenu: ImageView) {
        popupMenu = PopupMenu(context, expandedMenu)
        popupMenu.setOnMenuItemClickListener(this)
        println(binding.report?.status)
        when (binding.report?.status) {
        //FIXME this information will be handled locally
            0 -> {
                popupMenu.menuInflater.inflate(R.menu.resend_report, popupMenu.menu)
            }
            1 -> {
                popupMenu.menuInflater.inflate(R.menu.wait_evaluation, popupMenu.menu)
            }

        }

    }

}