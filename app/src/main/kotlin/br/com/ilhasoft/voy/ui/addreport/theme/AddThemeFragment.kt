package br.com.ilhasoft.voy.ui.addreport.theme

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater

import android.view.ViewGroup
import android.view.View
import android.widget.LinearLayout
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.FragmentAddThemeBinding
import br.com.ilhasoft.voy.databinding.ItemThemeBinding
import br.com.ilhasoft.voy.databinding.ItemTagThemeBinding
import br.com.ilhasoft.voy.databinding.DialogThemesBinding


import br.com.ilhasoft.voy.models.Tag
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.ui.addreport.theme.holder.TagViewHolder
import br.com.ilhasoft.voy.ui.addreport.theme.holder.ThemeViewHolder
import br.com.ilhasoft.voy.ui.base.BaseFragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class AddThemeFragment : BaseFragment(), AddThemeFragmentContract {

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
                .setNegativeButton(android.R.string.cancel) { _, _ ->
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.presenter = presenter
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTagsList(binding.tagsList)
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

    override fun toggleTagStatus() {

    }

    override fun showThemesDialog() {
        dialog.show()
    }

    override fun swapTheme(theme: Theme?) {
        presenter.setSelectedTheme(theme)
        themesAdapter.notifyDataSetChanged()
    }

    private fun dismissDialog() {
        if (dialog.isShowing) dialog.dismiss()
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
