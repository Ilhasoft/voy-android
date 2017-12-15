package br.com.ilhasoft.voy.ui.report.detail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.PopupMenu
import br.com.ilhasoft.support.core.helpers.DimensionHelper
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.support.recyclerview.decorations.SpaceItemDecoration
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityReportDetailBinding
import br.com.ilhasoft.voy.databinding.ItemTagBinding
import br.com.ilhasoft.voy.databinding.ViewReportToolbarBinding
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.Tag
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.comment.CommentsActivity
import br.com.ilhasoft.voy.ui.report.detail.holder.TagViewHolder
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class ReportDetailActivity : BaseActivity(), ReportDetailContract, PopupMenu.OnMenuItemClickListener {

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, ReportDetailActivity::class.java)
    }

    private val binding: ActivityReportDetailBinding by lazy {
        DataBindingUtil.setContentView<ActivityReportDetailBinding>(this, R.layout.activity_report_detail)
    }
    private val presenter: ReportDetailPresenter by lazy { ReportDetailPresenter() }
    private lateinit var popupMenu: PopupMenu
    private val tagViewHolder:
            OnCreateViewHolder<Tag, TagViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            TagViewHolder(ItemTagBinding.inflate(layoutInflater, parent, false))
        }
    }
    private val tagsAdapter:
            AutoRecyclerAdapter<Tag, TagViewHolder> by lazy {
        AutoRecyclerAdapter(mutableListOf(), tagViewHolder).apply {
            setHasStableIds(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.attachView(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun navigateBack() = onBackPressed()

    override fun showPopupMenu() {
        popupMenu.show()
    }

    override fun navigateToCommentReport() = startActivity(CommentsActivity.createIntent(this))

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.edit -> true
        R.id.share -> true
        else -> false
    }

    private fun setupView() {
        binding.run {
            report = Report()
            viewToolbar?.run { setupToolbar(this) }
            setupRecyclerView(tags)
            presenter = this@ReportDetailActivity.presenter
        }
    }

    private fun setupToolbar(viewToolbar: ViewReportToolbarBinding) = with(viewToolbar) {
        setupPopupMenu(expandedMenu)
        drawableId = R.drawable.ic_default_img_profile
        presenter = this@ReportDetailActivity.presenter
    }

    private fun setupPopupMenu(expandedMenu: ImageButton) {
        popupMenu = PopupMenu(this, expandedMenu)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.menuInflater.inflate(R.menu.report_detail, popupMenu.menu)
    }

    private fun setupRecyclerView(tags: RecyclerView) = with(tags) {
        layoutManager = setupLayoutManager()
        addItemDecoration(setupItemDecoration())
        setHasFixedSize(true)
        adapter = tagsAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager =
            FlexboxLayoutManager(this).apply {
                flexWrap = FlexWrap.WRAP
            }

    private fun setupItemDecoration(): SpaceItemDecoration {
        val space = DimensionHelper.toPx(this, 4f)
        return SpaceItemDecoration(0, 0, 2 * space, space)
    }

}
