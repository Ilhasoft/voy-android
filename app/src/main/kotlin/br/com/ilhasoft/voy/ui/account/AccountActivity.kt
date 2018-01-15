package br.com.ilhasoft.voy.ui.account

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import br.com.ilhasoft.support.core.helpers.DimensionHelper
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.support.recyclerview.decorations.LinearSpaceItemDecoration
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityAccountBinding
import br.com.ilhasoft.voy.databinding.ItemAvatarBinding
import br.com.ilhasoft.voy.models.SharedPreferences
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.login.LoginActivity

class AccountActivity : BaseActivity(), AccountContract {

    companion object {
        private const val AVATARS_SPAN_COUNT = 5

        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, AccountActivity::class.java)
    }

    private val binding: ActivityAccountBinding by lazy {
        DataBindingUtil.setContentView<ActivityAccountBinding>(this, R.layout.activity_account)
    }
    private val presenter: AccountPresenter by lazy { AccountPresenter(SharedPreferences(this)) }
    private val avatarViewHolder:
            OnCreateViewHolder<Int, AvatarViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            AvatarViewHolder(ItemAvatarBinding.inflate(layoutInflater, parent, false),
                    presenter)
        }
    }
    private val avatarsAdapter:
            AutoRecyclerAdapter<Int, AvatarViewHolder> by lazy {
        AutoRecyclerAdapter(mutableListOf(), avatarViewHolder).apply {
            setHasStableIds(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.attachView(this)
        presenter.setSelectedAvatar(binding.drawableId)
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

    override fun navigateBack() {
        onBackPressed()
    }

    override fun navigateToHome() {
        finish()
    }

    override fun navigateToSwitchAvatar() {
        binding.editingPhoto = binding.editingPhoto?.not()
    }

    override fun swapAvatar() {
        binding.drawableId = presenter.getSelectedAvatar()
        avatarsAdapter.notifyDataSetChanged()
    }

    override fun navigateToMakeLogout() = startActivity(LoginActivity.createIntent(this))

    private fun setupView() {
        binding.run {
            //FIXME set the actual user avatar not a static reference
            drawableId = R.drawable.ic_avatar12
            editingPhoto = false
            presenter = this@AccountActivity.presenter
            toolbar?.run {
                presenter = this@AccountActivity.presenter
            }
            setupAdapter()
            setupRecyclerView(avatars)
        }
    }

    private fun setupAdapter() {
        val avatars = resources.obtainTypedArray(R.array.avatars)
        (0 until avatars.length()).forEach { avatarsAdapter.add(avatars.getResourceId(it, 0)) }
    }

    private fun setupRecyclerView(reports: RecyclerView) = with(reports) {
        layoutManager = setupLayoutManager()
        addItemDecoration(setupItemDecoration())
        setHasFixedSize(true)
        adapter = avatarsAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager =
            GridLayoutManager(this, AVATARS_SPAN_COUNT)

    private fun setupItemDecoration(): LinearSpaceItemDecoration {
        val itemDecoration = LinearSpaceItemDecoration(LinearLayoutManager.VERTICAL)
        itemDecoration.space = DimensionHelper.toPx(this, 4f)
        return itemDecoration
    }

}
