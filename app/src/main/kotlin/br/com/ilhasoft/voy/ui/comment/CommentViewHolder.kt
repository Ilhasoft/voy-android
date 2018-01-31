package br.com.ilhasoft.voy.ui.comment

import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ItemCommentBinding

/**
 * Created by developer on 14/12/17.
 */
class CommentViewHolder(
        private val binding: ItemCommentBinding,
        private val presenter: CommentsPresenter
) : ViewHolder<CommentUIModel>(binding.root),
        PopupMenu.OnMenuItemClickListener {

    override fun onBind(comment: CommentUIModel) {
        binding.comment = comment
        binding.isMyComment = presenter.isMyID(comment.userId)
        val popupMenu = PopupMenu(context, binding.expandedMenu)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.menuInflater.inflate(R.menu.comments, popupMenu.menu)
        binding.expandedMenu.setOnClickListener(onMoreOptionsClick(popupMenu))
        binding.executePendingBindings()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.remove -> {
            binding.comment?.let { presenter.onClickRemoveComment(it) }
            true
        }

        else -> false
    }

    private fun onMoreOptionsClick(popupMenu: PopupMenu): (View) -> Unit = {
        popupMenu.show()
    }

}