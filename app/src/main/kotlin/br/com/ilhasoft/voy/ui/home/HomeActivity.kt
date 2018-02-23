package br.com.ilhasoft.voy.ui.home

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.support.recyclerview.decorations.SpaceItemDecoration
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityHomeBinding
import br.com.ilhasoft.voy.databinding.ItemMapBinding
import br.com.ilhasoft.voy.databinding.ItemNotificationBinding
import br.com.ilhasoft.voy.databinding.ItemThemeBinding
import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.models.SharedPreferences
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.shared.helpers.ResourcesHelper
import br.com.ilhasoft.voy.ui.account.AccountActivity
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.home.holder.NotificationViewHolder
import br.com.ilhasoft.voy.ui.home.holder.ProjectViewHolder
import br.com.ilhasoft.voy.ui.home.holder.ThemeViewHolder
import br.com.ilhasoft.voy.ui.report.ReportsActivity
import br.com.ilhasoft.voy.ui.report.detail.ReportDetailActivity

class HomeActivity : BaseActivity(), HomeContract {

    companion object {
        private const val ACCOUNT_REQUEST_CODE = 100

        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

    private val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
    }
    private val presenter: HomePresenter by lazy { HomePresenter(SharedPreferences(this), HomeInteractorImpl()) }
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
    private val themeViewHolder: OnCreateViewHolder<Theme, ThemeViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            ThemeViewHolder(ItemThemeBinding.inflate(layoutInflater, parent, false), presenter)
        }
    }
    private val themesAdapter: AutoRecyclerAdapter<Theme, ThemeViewHolder> by lazy {
        AutoRecyclerAdapter<Theme, ThemeViewHolder>(themeViewHolder).apply {
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
        if (projects.size > 0) {
            binding.viewToolbar?.projectName = projects.first().name
            projectsAdapter.clear()
            projectsAdapter.addAll(projects)
        } else {
            projectsAdapter.clear()
        }
    }

    override fun fillThemesAdapter(themes: MutableList<Theme>) {
        binding.themesQuantity = themes.size
        themesAdapter.clear()
        themesAdapter.addAll(themes)
    }

    override fun fillNotificationAdapter(notifications: List<Notification>) {
        notificationsAdapter.clear()
        notificationsAdapter.addAll(notifications)
        binding.viewToolbar?.hasNotification = notificationsAdapter.isNotEmpty()
    }

    override fun navigateToMyAccount() = startActivityForResult(AccountActivity.createIntent(this), ACCOUNT_REQUEST_CODE)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACCOUNT_REQUEST_CODE) {
            val position = presenter.getAvatarPositionFromPreferences()
            binding.viewToolbar?.drawableResId = ResourcesHelper.getAvatarsResources(this)[position]
        }
    }
    override fun selectProject() {
        binding.selectingProject = binding.selectingProject?.not()
    }

    override fun swapProject(project: Project) {
        binding.run {
            selectingProject = selectingProject?.not()
            viewToolbar?.projectName = project.name
        }
        projectsAdapter.notifyDataSetChanged()
    }

    override fun showNotifications() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
    }

    override fun dismissNotifications() {
        binding.drawerLayout.closeDrawer(GravityCompat.END)
    }

    override fun navigateToNotificationDetail(notification: Notification) {
        startActivity(ReportDetailActivity.createIntent(this, notification.report))
        notificationsAdapter.remove(notification)
        binding.viewToolbar?.hasNotification = notificationsAdapter.isNotEmpty()
    }

    override fun navigateToThemeReports(theme: Theme) {
        startActivity(ReportsActivity.createIntent(this, theme.id, theme.name, theme.color, theme.bounds))
    }

    private fun setupView() {
        binding.apply {
            selectingProject = false
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
        setupToolbar()
        setupProjectsRecyclerView()
        setupNotificationsRecyclerView()
        setupThemesRecyclerView()
    }

    private fun setupToolbar() = with(binding.viewToolbar) {
        this!!.run {
            hasNotification = notificationsAdapter.isNotEmpty()
            presenter = this@HomeActivity.presenter
            val position = this@HomeActivity.presenter.getAvatarPositionFromPreferences()
            drawableResId = ResourcesHelper.getAvatarsResources(this@HomeActivity)[position]
        }
    }

    private fun setupProjectsRecyclerView() = with(binding.projects) {
        layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
        setHasFixedSize(true)
        adapter = projectsAdapter
    }

    private fun setupNotificationsRecyclerView() = with(binding.notifications) {
        layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
        addItemDecoration(DividerItemDecoration(this@HomeActivity, DividerItemDecoration.VERTICAL))
        adapter = notificationsAdapter
    }

    private fun setupThemesRecyclerView() = with(binding.themes) {
        layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
        setHasFixedSize(true)
        addItemDecoration(SpaceItemDecoration(0, 0, 0, 32))
        adapter = themesAdapter
    }

}
