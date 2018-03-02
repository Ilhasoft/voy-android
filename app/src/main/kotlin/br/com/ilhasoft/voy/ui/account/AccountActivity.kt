package br.com.ilhasoft.voy.ui.account

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import br.com.ilhasoft.support.core.helpers.DimensionHelper
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.support.recyclerview.decorations.LinearSpaceItemDecoration
import br.com.ilhasoft.support.validation.Validator
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityAccountBinding
import br.com.ilhasoft.voy.databinding.ItemAvatarBinding
import br.com.ilhasoft.voy.models.SharedPreferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.users.UserRepository
import br.com.ilhasoft.voy.network.users.UserService
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
    private val presenter: AccountPresenter by lazy {
        AccountPresenter(AccountInteractorImpl(
                SharedPreferences(this@AccountActivity),
                UserRepository(UserService())))
    }
    private val avatarViewHolder: OnCreateViewHolder<Int, AvatarViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            AvatarViewHolder(ItemAvatarBinding.inflate(layoutInflater, parent, false),
                    presenter)
        }
    }
    private val avatarsAdapter: AutoRecyclerAdapter<Int, AvatarViewHolder> by lazy {
        AutoRecyclerAdapter(mutableListOf(), avatarViewHolder).apply {
            setHasStableIds(true)
        }
    }

    private val validator by lazy { Validator(binding) }
    private val isPasswordLocked by lazy { ObservableBoolean(true) }

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

    override fun navigateBack() {
        onBackPressed()
    }

    override fun navigateToHome() {
        finish()
    }

    override fun navigateToSwitchAvatar() {
        binding.editingPhoto = binding.editingPhoto?.not()
    }

    override fun swapAvatar(newAvatar: Int, position: Int) {
        binding.drawableId = newAvatar
        avatarsAdapter.notifyDataSetChanged()
    }

    override fun setUser(user: User) {
        binding.user = user
    }

    override fun saveUser() {
        binding.user?.let {
            val newUser = it.copy(avatar = "${getAvatarFromSelectedResource().plus(1)}")
            presenter.saveUser(newUser)
        }
    }

    override fun isValidUser(): Boolean = isPasswordLocked.get() || validator.validate()

    override fun userUpdatedMessage() {
        showMessage(R.string.user_updated_message)
    }

    override fun setNewAvatarToUser(position: Int) {
        val arrayPosition = position.minus(1)
        presenter.setSelectedAvatar(getAvatarsResources()[arrayPosition], arrayPosition)
    }

    override fun setAvatarByPosition(position: Int) {
        val arrayPosition = position.minus(1)
        binding.drawableId = getAvatarsResources()[arrayPosition]
        presenter.avatarDrawableId = binding.drawableId
    }

    override fun changeLockState() {
        isPasswordLocked.set(!isPasswordLocked.get())
        binding.user?.let { it.password = null }
    }

    override fun navigateToMakeLogout() = startActivity(LoginActivity.createIntent(this))

    private fun setupView() {
        binding.apply {
            editingPhoto = false
            presenter = this@AccountActivity.presenter
            isPasswordLocked = this@AccountActivity.isPasswordLocked
        }

        setupAdapter()
        setupRecyclerView(binding.avatars)
    }

    private fun setupAdapter() {
        avatarsAdapter.addAll(getAvatarsResources())
    }

    private fun getAvatarsResources(): List<Int> {
        val avatars = resources.obtainTypedArray(R.array.avatars)
        val resources = (0 until avatars.length()).map { avatars.getResourceId(it, 0) }
        avatars.recycle()
        return resources
    }

    private fun getAvatarFromSelectedResource(): Int = getAvatarsResources().indexOf(presenter.avatarDrawableId)

    private fun setupRecyclerView(reports: RecyclerView) = with(reports) {
        setHasFixedSize(true)
        layoutManager = GridLayoutManager(this@AccountActivity, AVATARS_SPAN_COUNT)
        addItemDecoration(LinearSpaceItemDecoration(LinearLayoutManager.VERTICAL).apply {
            space = DimensionHelper.toPx(this@AccountActivity, 4f)
        })
        adapter = avatarsAdapter
    }

}
