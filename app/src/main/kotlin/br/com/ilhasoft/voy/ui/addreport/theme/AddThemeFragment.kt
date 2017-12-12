package br.com.ilhasoft.voy.ui.addreport.theme

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater

import android.view.ViewGroup
import android.view.View
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.voy.R


import br.com.ilhasoft.voy.databinding.FragmentAddThemeBinding
import br.com.ilhasoft.voy.databinding.ItemTagBinding
import br.com.ilhasoft.voy.ui.base.BaseFragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class AddThemeFragment : BaseFragment(), AddThemeFragmentContract, OnCreateViewHolder<String, TagViewHolder> {

    private val binding: FragmentAddThemeBinding by lazy {
        FragmentAddThemeBinding.inflate(LayoutInflater.from(context))
    }

    private val presenter: AddThemeFragmentPresenter by lazy { AddThemeFragmentPresenter() }

    private val tagsAdapter: AutoRecyclerAdapter<String, TagViewHolder> by lazy {
        AutoRecyclerAdapter<String, TagViewHolder>(this).apply {
            setHasStableIds(true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

    override fun onCreateViewHolder(layoutInflater: LayoutInflater,
                                    parent: ViewGroup?, viewType: Int): TagViewHolder {
        return TagViewHolder(ItemTagBinding.inflate(layoutInflater,
                parent, false), presenter)
    }

    override fun toggleTagStatus() {

    }

    private fun setupTagsList(tagsList: RecyclerView) = with(tagsList) {
        layoutManager = setupLayoutManager()
        tagsAdapter.addAll(resources.getStringArray(R.array.tags))
        adapter = tagsAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager? {
        val layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)
        layoutManager.justifyContent = JustifyContent.FLEX_START
        return layoutManager
    }

}
