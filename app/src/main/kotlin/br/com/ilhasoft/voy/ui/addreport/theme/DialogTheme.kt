package br.com.ilhasoft.voy.ui.addreport.theme

import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.voy.databinding.DialogThemesBinding
import br.com.ilhasoft.voy.databinding.ItemThemeBinding
import br.com.ilhasoft.voy.models.Theme

/**
 * Created by geral on 13/12/17.
 */
class DialogTheme : DialogFragment() {

    private val binding: DialogThemesBinding by lazy {
        DialogThemesBinding.inflate(LayoutInflater.from(context))
    }

    private val presenter: AddThemeFragmentPresenter by lazy { AddThemeFragmentPresenter() }

    private val themeViewHolder: OnCreateViewHolder<Theme, ThemeViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            ThemeViewHolder(ItemThemeBinding.inflate(layoutInflater, parent, false), presenter)
        }
    }

    private val themeAutoAdapter: AutoRecyclerAdapter<Theme, ThemeViewHolder> by lazy {
        AutoRecyclerAdapter<Theme, ThemeViewHolder>(themeViewHolder).apply {
            setHasStableIds(true)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.run {
            presenter = this@DialogTheme.presenter
        }
        setupThemeList(binding.themes)
    }

    private fun setupThemeList(recyclerView: RecyclerView) = with(recyclerView) {
        layoutManager = setupLayoutManager()
        addItemDecoration(setItemDecoration())
        adapter = themeAutoAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager? =
            LinearLayoutManager(context, LinearLayout.VERTICAL, false)

    private fun setItemDecoration(): RecyclerView.ItemDecoration? =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

}