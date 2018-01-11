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
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityHomeBinding
import br.com.ilhasoft.voy.databinding.ItemMapBinding
import br.com.ilhasoft.voy.databinding.ItemNotificationBinding
import br.com.ilhasoft.voy.databinding.ViewHomeToolbarBinding
import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.ui.account.AccountActivity
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.home.holder.ProjectViewHolder

class HomeActivity : BaseActivity(), HomeContract {

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

    private val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
    }
    private val presenter: HomePresenter by lazy { HomePresenter() }
    private val projectViewHolder: OnCreateViewHolder<Project, ProjectViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            ProjectViewHolder(ItemMapBinding.inflate(layoutInflater, parent, false), presenter)
        }
    }
    private val projectsAdapter: AutoRecyclerAdapter<Project, ProjectViewHolder> by lazy {
        AutoRecyclerAdapter(mutableListOf(), projectViewHolder).apply {
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

    override fun fillProjectsAdapter(projects: MutableList<Project>) {
        binding.viewToolbar?.project = projects.first()
        projectsAdapter.addAll(projects)
        projectsAdapter.notifyDataSetChanged()
    }

    override fun navigateToMyAccount() = startActivity(AccountActivity.createIntent(this))

    override fun selectProject() {
        binding.selectingProject = binding.selectingProject?.not()
    }

    override fun swapProject(project: Project?) {
        binding.run {
            selectingProject = selectingProject?.not()
            viewToolbar?.project = project
            this@HomeActivity.presenter.setSelectedProject(project)
        }
        projectsAdapter.notifyDataSetChanged()
    }

    override fun showNotifications() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
    }

    override fun dismissNotifications() {
        binding.drawerLayout.closeDrawer(GravityCompat.END)
    }

    override fun navigateToNotificationDetail() {

    }

    private fun setupView() {
        binding.apply {
            selectingProject = false
            viewToolbar?.run { setupToolbar(this) }
            setupProjectsRecyclerView(projects)
            setupDrawer()
            setupNotifications(notifications)
        }
    }

    private fun setupToolbar(viewToolbar: ViewHomeToolbarBinding) = with(viewToolbar) {
        hasNotification = notificationsAdapter.count() > 0
        presenter = this@HomeActivity.presenter
        this@HomeActivity.presenter.setSelectedProject(project)
    }

    private fun setupProjectsRecyclerView(projects: RecyclerView) = with(projects) {
        layoutManager = setupLayoutManager()
        setHasFixedSize(true)
        adapter = projectsAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    private fun setupDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun setupNotifications(recyclerView: RecyclerView) = with(recyclerView) {
        layoutManager = setupLayoutManager()
        addItemDecoration(setupItemDecoration())
        adapter = notificationsAdapter
    }

    private fun setupItemDecoration(): RecyclerView.ItemDecoration? =
            DividerItemDecoration(this@HomeActivity, DividerItemDecoration.VERTICAL)

}
