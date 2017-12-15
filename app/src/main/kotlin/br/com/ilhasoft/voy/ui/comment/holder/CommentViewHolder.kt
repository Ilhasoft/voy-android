package br.com.ilhasoft.voy.ui.comment.holder

import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ItemCommentBinding
import br.com.ilhasoft.voy.models.Comment
import br.com.ilhasoft.voy.ui.comment.CommentsPresenter

/**
 * Created by developer on 14/12/17.
 */
class CommentViewHolder(val binding: ItemCommentBinding,
                        val presenter: CommentsPresenter) : ViewHolder<Comment>(binding.root),
        View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var popupMenu: PopupMenu

    init {
        binding.expandedMenu.run {
            setupPopupMenu(this)
            this.setOnClickListener(this@CommentViewHolder)
        }
    }

    override fun onBind(comment: Comment?) {
        binding.comment = comment
        binding.executePendingBindings()
    }

    override fun onClick(v: View?) {
        popupMenu.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.edit -> {
            presenter.onClickEditComment(binding.comment)
            true
        }
        R.id.share -> {
            presenter.onClickRemoveComment(binding.comment)
            true
        }
        else -> false
    }

    private fun setupPopupMenu(expandedMenu: ImageView) {
        popupMenu = PopupMenu(context, expandedMenu)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.menuInflater.inflate(R.menu.comments, popupMenu.menu)
    }

}