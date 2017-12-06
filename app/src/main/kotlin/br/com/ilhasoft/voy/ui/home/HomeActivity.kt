package br.com.ilhasoft.voy.ui.home

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityHomeBinding
import br.com.ilhasoft.voy.databinding.ItemMapBinding
import br.com.ilhasoft.voy.databinding.ViewHomeToolbarBinding
import br.com.ilhasoft.voy.models.Map
import br.com.ilhasoft.voy.ui.account.AccountActivity
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.home.adapter.HomeAdapter
import br.com.ilhasoft.voy.ui.home.adapter.NavigationItem
import br.com.ilhasoft.voy.ui.home.holder.MapViewHolder
import br.com.ilhasoft.voy.ui.report.ReportsFragment

class HomeActivity : BaseActivity(), HomeContract {

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

    private val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
    }
    private val presenter: HomePresenter by lazy { HomePresenter() }
    private val mapViewHolder: OnCreateViewHolder<Map, MapViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            MapViewHolder(ItemMapBinding.inflate(layoutInflater, parent, false), presenter)
        }
    }
    private val mapsAdapter: AutoRecyclerAdapter<Map, MapViewHolder> by lazy {
        AutoRecyclerAdapter(mutableListOf(), mapViewHolder).apply {
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

    override fun navigateToMyAccount() = startActivity(AccountActivity.createIntent(this))

    override fun selectMap() {
        binding.selectingThemes = binding.selectingThemes?.not()
    }

    override fun showNotifications() {

    }

    private fun setupView() {
        binding.apply {
            selectingThemes = false
            viewToolbar?.run { setupToolbar(this) }
            setupRecyclerView(maps)
            setupTabs()
        }
    }

    private fun setupToolbar(viewToolbar: ViewHomeToolbarBinding) = with(viewToolbar) {
        map = Map()
        presenter = this@HomeActivity.presenter
    }

    private fun setupRecyclerView(maps: RecyclerView) = with(maps) {
        layoutManager = setupLayoutManager()
        setHasFixedSize(true)
        adapter = mapsAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    private fun setupTabs() {
        val adapter = HomeAdapter(supportFragmentManager, createNavigationItems())
        binding.apply {
            viewPager.let {
                it.adapter = adapter
                it.offscreenPageLimit = adapter.count
            }
            tabLayout.setupWithViewPager(binding.viewPager)
        }
    }

    private fun createNavigationItems(): MutableList<NavigationItem> {
        val approved = NavigationItem(ReportsFragment.newInstance(getString(R.string.approved_fragment_title)),
                getString(R.string.approved_fragment_title))
        val pending = NavigationItem(ReportsFragment.newInstance(getString(R.string.pending_fragment_title)),
                getString(R.string.pending_fragment_title))
        val rejected = NavigationItem(ReportsFragment.newInstance(getString(R.string.rejected_fragment_title)),
                getString(R.string.rejected_fragment_title))
        return mutableListOf(approved, pending, rejected)
    }

}
