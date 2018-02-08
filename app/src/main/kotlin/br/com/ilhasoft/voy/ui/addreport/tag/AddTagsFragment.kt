package br.com.ilhasoft.voy.ui.addreport.tag

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.FragmentAddTagBinding
import br.com.ilhasoft.voy.databinding.ItemTagBinding
import br.com.ilhasoft.voy.models.TagDataUI
import br.com.ilhasoft.voy.ui.addreport.ReportViewModel
import br.com.ilhasoft.voy.ui.base.BaseFragment
import br.com.ilhasoft.voy.ui.shared.TagViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class AddTagsFragment : BaseFragment() {

    private val binding: FragmentAddTagBinding by lazy {
        FragmentAddTagBinding.inflate(LayoutInflater.from(context))
    }

    private val tagDataUI : TagDataUI by lazy { setupTagData() }

    private val reportViewModel by lazy { ViewModelProviders.of(activity).get(ReportViewModel::class.java) }

    private val tagsAdapter by lazy {
        AutoRecyclerAdapter<String, TagViewHolder>(tagsViewHolder).apply {
            setHasStableIds(true)
        }
    }
    private val tagsViewHolder: OnCreateViewHolder<String, TagViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            TagViewHolder(ItemTagBinding.inflate(layoutInflater, parent, false), tagDataUI, reportViewModel)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupTagsRecyclerView()
        reportViewModel.getAllTags().observe(activity, Observer { list ->
            list?.let { setTags(it) }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        reportViewModel.setButtonEnable(true)
        reportViewModel.setButtonTitle(R.string.send_report)
    }

    private fun setupTagData(): TagDataUI {
        val tagDataUI = TagDataUI
        tagDataUI.selectedColor = ContextCompat.getColor(context, R.color.bright_sky_blue)
        tagDataUI.textSelectedColor = ContextCompat.getColor(context, R.color.white_three)
        tagDataUI.normalColor = ContextCompat.getColor(context, R.color.white_five)
        tagDataUI.textNormalColor = ContextCompat.getColor(context, R.color.black)
        tagDataUI.textSize = 12F
        return tagDataUI
    }

    private fun setTags(tagsList: List<String>) {
        tagsAdapter.clear()
        tagsAdapter.addAll(tagsList)
    }

    private fun setupTagsRecyclerView() = with(binding.tags) {
        layoutManager = setupLayoutManager()
        setHasFixedSize(true)
        adapter = tagsAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager? {
        val layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)
        layoutManager.justifyContent = JustifyContent.FLEX_START
        return layoutManager
    }

}
