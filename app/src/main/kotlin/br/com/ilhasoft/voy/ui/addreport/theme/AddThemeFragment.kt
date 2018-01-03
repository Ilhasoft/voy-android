package br.com.ilhasoft.voy.ui.addreport.theme

import android.app.Dialog
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.DialogThemesBinding
import br.com.ilhasoft.voy.databinding.FragmentAddThemeBinding
import br.com.ilhasoft.voy.databinding.ItemTagThemeBinding
import br.com.ilhasoft.voy.databinding.ItemThemeBinding
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.Tag
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.ui.addreport.AddReportActivity
import br.com.ilhasoft.voy.ui.addreport.theme.holder.TagViewHolder
import br.com.ilhasoft.voy.ui.addreport.theme.holder.ThemeViewHolder
import br.com.ilhasoft.voy.ui.base.BaseFragment
import br.com.ilhasoft.voy.ui.shared.OnReportChangeListener
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class AddThemeFragment : BaseFragment(), AddThemeFragmentContract {

    companion object {
        const val TAG = "Theme"
    }

    private val binding: FragmentAddThemeBinding by lazy {
        FragmentAddThemeBinding.inflate(LayoutInflater.from(context))
    }

    private val presenter: AddThemeFragmentPresenter by lazy { AddThemeFragmentPresenter() }

    private val tagsAdapter: AutoRecyclerAdapter<Tag, TagViewHolder> by lazy {
        AutoRecyclerAdapter<Tag, TagViewHolder>(tagsViewHolder).apply {
            setHasStableIds(true)
        }
    }

    private val tagsViewHolder: OnCreateViewHolder<Tag, TagViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            TagViewHolder(ItemTagThemeBinding.inflate(layoutInflater, parent, false), presenter)
        }
    }

    private val themeDialogBinding: DialogThemesBinding by lazy {
        DialogThemesBinding.inflate(LayoutInflater.from(context), null, false)
    }

    private val dialog by lazy {
        setupThemesList(themeDialogBinding.themes)
        AlertDialog.Builder(context).setView(themeDialogBinding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    binding.themes.text = presenter.getSelectedTheme()?.name
                }
                .setNegativeButton(android.R.string.no) { _, _ ->
                    dismissDialog()
                }
                .create()
    }

    private val themesAdapter: AutoRecyclerAdapter<Theme, ThemeViewHolder> by lazy {
        AutoRecyclerAdapter<Theme, ThemeViewHolder>(themesViewHolder).apply {
            setHasStableIds(true)
        }
    }

    private val themesViewHolder: OnCreateViewHolder<Theme, ThemeViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            ThemeViewHolder(ItemThemeBinding.inflate(layoutInflater, parent, false), presenter)
        }
    }

    private val reportListener: OnReportChangeListener by lazy { activity as AddReportActivity }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        reportListener.changeActionButtonName(R.string.send)
        setupView()
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTagsList(binding.tagsList)
        presenter.attachView(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.setReportReference(arguments.getParcelable(Report.TAG))
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onResume() {
        super.onResume()
        reportListener.changeActionButtonStatus(false)
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showThemesDialog() {
        dialog.show()
        dialog.getButton(Dialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(context, R.color.cool_grey))
    }

    override fun swapTheme(theme: Theme?) {
        presenter.setSelectedTheme(theme)
        themesAdapter.notifyDataSetChanged()
    }

    override fun changeActionButtonStatus(status: Boolean) {
        reportListener.changeActionButtonStatus(status)
    }

    override fun notifyTagsListChange() {
        tagsAdapter.notifyDataSetChanged()
    }

    private fun dismissDialog() {
        if (dialog.isShowing) dialog.dismiss()
    }

    fun setupView() {
        binding.run {
            presenter = this@AddThemeFragment.presenter
        }
    }

    private fun setupTagsList(tagsList: RecyclerView) = with(tagsList) {
        layoutManager = setupLayoutManager()
        adapter = tagsAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager? {
        val layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)
        layoutManager.justifyContent = JustifyContent.FLEX_START
        return layoutManager
    }

    private fun setupThemesList(themes: RecyclerView) = with(themes) {
        layoutManager = setupLayoutManagerThemes()
        addItemDecoration(setItemDecoration())
        adapter = themesAdapter
    }

    private fun setupLayoutManagerThemes(): RecyclerView.LayoutManager? =
            LinearLayoutManager(context, LinearLayout.VERTICAL, false)

    private fun setItemDecoration(): RecyclerView.ItemDecoration? =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)


}
