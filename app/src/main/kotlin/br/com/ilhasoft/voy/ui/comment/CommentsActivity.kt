package br.com.ilhasoft.voy.ui.comment

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import br.com.ilhasoft.support.core.helpers.KeyboardHelper
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.support.validation.Validator
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityCommentsBinding
import br.com.ilhasoft.voy.databinding.ItemCommentBinding
import br.com.ilhasoft.voy.models.SharedPreferences
import br.com.ilhasoft.voy.network.comments.CommentRepository
import br.com.ilhasoft.voy.network.comments.CommentsService
import br.com.ilhasoft.voy.ui.base.BaseActivity

class CommentsActivity : BaseActivity(), CommentsContract {

    companion object {
        private const val REPORT_ID_EXTRA = "reportIdExtra"

        @JvmStatic
        fun createIntent(context: Context, reportId: Int): Intent = Intent(context,
                CommentsActivity::class.java).putExtra(REPORT_ID_EXTRA, reportId)
    }

    private val binding: ActivityCommentsBinding by lazy {
        DataBindingUtil.setContentView<ActivityCommentsBinding>(this, R.layout.activity_comments)
    }
    private val presenter: CommentsPresenter by lazy {
        val mapper = CommentsUIMapper()
        CommentsPresenter(CommentRepository(CommentsService()), mapper, SharedPreferences(applicationContext))
    }
    private val reportCommentViewHolder: OnCreateViewHolder<CommentUIModel, CommentViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            CommentViewHolder(
                    ItemCommentBinding.inflate(layoutInflater, parent, false), presenter
            )
        }
    }
    private val commentsAdapter: AutoRecyclerAdapter<CommentUIModel, CommentViewHolder> by lazy {
        AutoRecyclerAdapter(mutableListOf(), reportCommentViewHolder).apply {
            setHasStableIds(true)
        }
    }

    private val reportId by lazy { intent.getIntExtra(REPORT_ID_EXTRA, 0) }
    private val progressObserver: ObservableBoolean by lazy { ObservableBoolean(true) }
    private val emptyViewObserver: ObservableBoolean by lazy { ObservableBoolean(false) }
    private val validator: Validator by lazy { Validator(binding) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.attachView(this)
        presenter.loadComments(reportId)
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

    override fun navigateBack() = onBackPressed()

    override fun navigateToRemoveComment(comment: CommentUIModel) {
        commentsAdapter.remove(comment)
    }

    override fun showLoading() {
        progressObserver.set(true)
    }

    override fun dismissLoading() {
        progressObserver.set(false)
    }

    override fun showEmptyView() {
        emptyViewObserver.set(true)
    }

    override fun dismissEmptyView() {
        emptyViewObserver.set(false)
    }

    override fun populateComments(comments: List<CommentUIModel>) {
        commentsAdapter.addAll(comments)
    }

    override fun commentCreated() {
        cleanKeyboard()
        showMessage(R.string.comment_created_message)
    }

    private fun cleanKeyboard() {
        KeyboardHelper.hideForced(binding.comment)
        binding.comment.setText("")
    }

    override fun isValidCommentBodyState(): Boolean = validator.validate()

    private fun setupView() {
        binding.apply {
            reportId = this@CommentsActivity.reportId
            inProgress = progressObserver
            showEmptyView = emptyViewObserver
            presenter = this@CommentsActivity.presenter
        }

        setupRecyclerView(binding.comments)
    }

    private fun setupRecyclerView(comments: RecyclerView) = with(comments) {
        layoutManager = LinearLayoutManager(this@CommentsActivity)
        setHasFixedSize(true)
        adapter = commentsAdapter
    }

}
