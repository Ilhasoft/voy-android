package br.com.ilhasoft.voy.ui.home

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityHomeBinding
import br.com.ilhasoft.voy.ui.account.AccountActivity
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.home.adapter.HomeAdapter
import br.com.ilhasoft.voy.ui.home.adapter.NavigationItem
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

    override fun showNotifications() {

    }

    private fun setupView() {
        setupTabs()
    }

    private fun setupTabs() {
        val adapter = HomeAdapter(supportFragmentManager, createNavigationItems())
        binding.apply {
            viewToolbar?.run {
                presenter = this@HomeActivity.presenter
            }
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
