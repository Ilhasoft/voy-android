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
import br.com.ilhasoft.voy.models.ReportComment
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
    private val reportCommentViewHolder:
            OnCreateViewHolder<ReportComment, CommentViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            CommentViewHolder(ItemCommentBinding.inflate(layoutInflater, parent, false),
                    presenter)
        }
    }
    private val commentsAdapter:
            AutoRecyclerAdapter<ReportComment, CommentViewHolder> by lazy {
        AutoRecyclerAdapter(mutableListOf(), reportCommentViewHolder).apply {
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

    override fun navigateToEditComment(reportComment: ReportComment?) {}

    override fun navigateToRemoveComment(reportComment: ReportComment?) {}

    override fun sendComment(reportComment: ReportComment?) {}

    private fun setupView() {
        binding.run {
            setupRecyclerView(comments)
            presenter = this@CommentsActivity.presenter
        }
    }

    private fun setupRecyclerView(comments: RecyclerView) = with(comments) {
        layoutManager = LinearLayoutManager(this@CommentsActivity, LinearLayoutManager.VERTICAL, false)
        setHasFixedSize(true)
        adapter = commentsAdapter
    }

}
