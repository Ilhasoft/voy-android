package br.com.ilhasoft.voy.ui.home

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityHomeBinding
import br.com.ilhasoft.voy.databinding.ItemMapBinding
import br.com.ilhasoft.voy.databinding.ItemNotificationBinding
import br.com.ilhasoft.voy.databinding.ViewHomeToolbarBinding
import br.com.ilhasoft.voy.models.Map
import br.com.ilhasoft.voy.models.Notification
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
    private val notificationViewHolder: OnCreateViewHolder<Notification, NotificationViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            NotificationViewHolder(ItemNotificationBinding
                    .inflate(layoutInflater, parent, false), presenter)
        }
    }
    private val notificationsAdapter: AutoRecyclerAdapter<Notification, NotificationViewHolder> by lazy {
        AutoRecyclerAdapter(mutableListOf(), notificationViewHolder).apply {
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

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

    override fun navigateToMyAccount() = startActivity(AccountActivity.createIntent(this))

    override fun selectMap() {
        binding.selectingMap = binding.selectingMap?.not()
    }

    override fun showNotifications() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
    }

    override fun dismissNotifications() {
        binding.drawerLayout.closeDrawer(GravityCompat.END)
    }

    override fun swapMap(map: Map?) {
        binding.run {
            selectingMap = selectingMap?.not()
            viewToolbar?.map = map
        }
    }

    override fun navigateToNotificationDetail() {

    }

    private fun setupView() {
        binding.apply {
            selectingMap = false
            viewToolbar?.run { setupToolbar(this) }
            setupRecyclerView(maps)
            setupTabs()
            setupDrawer()
            setupNotifications(notifications)
        }
    }

    private fun setupToolbar(viewToolbar: ViewHomeToolbarBinding) = with(viewToolbar) {
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
            presenter = this@HomeActivity.presenter
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

    private fun setupDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun setupNotifications(recyclerView: RecyclerView) = with(recyclerView) {
        layoutManager = setLayoutManager()
        addItemDecoration(setItemDecoration())
        adapter = notificationsAdapter
    }

    private fun setLayoutManager(): RecyclerView.LayoutManager? =
            LinearLayoutManager(this@HomeActivity, LinearLayout.VERTICAL, false)

    private fun setItemDecoration(): RecyclerView.ItemDecoration? =
            DividerItemDecoration(this@HomeActivity, DividerItemDecoration.VERTICAL)

}
