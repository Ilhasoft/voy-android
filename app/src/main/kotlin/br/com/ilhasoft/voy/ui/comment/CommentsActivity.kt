package br.com.ilhasoft.voy.ui.comment

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityCommentsBinding
import br.com.ilhasoft.voy.databinding.ItemCommentBinding
import br.com.ilhasoft.voy.databinding.ViewCommentsToolbarBinding
import br.com.ilhasoft.voy.models.Comment
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.comment.holder.CommentViewHolder

class CommentsActivity : BaseActivity(), CommentsContract {

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, CommentsActivity::class.java)
    }

    private val binding: ActivityCommentsBinding by lazy {
        DataBindingUtil.setContentView<ActivityCommentsBinding>(this, R.layout.activity_comments)
    }
    private val presenter: CommentsPresenter by lazy { CommentsPresenter() }
    private val commentViewHolder:
            OnCreateViewHolder<Comment, CommentViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            CommentViewHolder(ItemCommentBinding.inflate(layoutInflater, parent, false),
                    presenter)
        }
    }
    private val commentsAdapter:
            AutoRecyclerAdapter<Comment, CommentViewHolder> by lazy {
        AutoRecyclerAdapter(mutableListOf(), commentViewHolder).apply {
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

    override fun navigateBack() = onBackPressed()

    override fun navigateToEditComment(comment: Comment?) {}

    override fun navigateToRemoveComment(comment: Comment?) {}

    private fun setupView() {
        binding.run {
            viewToolbar?.run { setupToolbar(this) }
            setupRecyclerView(comments)
            presenter = this@CommentsActivity.presenter
        }
    }

    private fun setupToolbar(viewToolbar: ViewCommentsToolbarBinding) = with(viewToolbar) {
        presenter = this@CommentsActivity.presenter
    }

    private fun setupRecyclerView(comments: RecyclerView) = with(comments) {
        layoutManager = setupLayoutManager()
        setHasFixedSize(true)
        adapter = commentsAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

}
