package br.com.ilhasoft.voy.ui.addreport.tag

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.FragmentAddThemeBinding
import br.com.ilhasoft.voy.databinding.ItemTagThemeBinding
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.Tag
import br.com.ilhasoft.voy.ui.addreport.AddReportActivity
import br.com.ilhasoft.voy.ui.addreport.tag.holder.TagViewHolder
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

    private val reportListener: OnReportChangeListener by lazy { activity as AddReportActivity }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        reportListener.changeActionButtonName(R.string.send_report)
        setupView()
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun changeActionButtonStatus(status: Boolean) {
        reportListener.changeActionButtonStatus(status)
    }

    override fun notifyTagsChange() {
        tagsAdapter.notifyDataSetChanged()
    }

    fun setupView() {
        binding.run { setupTagsRecyclerView(tags) }
    }

    private fun setupTagsRecyclerView(tags: RecyclerView) = with(tags) {
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
