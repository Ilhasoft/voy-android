package br.com.ilhasoft.voy.ui.comment.holder

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemCommentBinding
import br.com.ilhasoft.voy.models.Comment
import br.com.ilhasoft.voy.ui.comment.CommentsPresenter

/**
 * Created by developer on 14/12/17.
 */
class CommentViewHolder(val binding: ItemCommentBinding,
                        val presenter: CommentsPresenter) : ViewHolder<Comment>(binding.root) {

    init {
        binding.presenter = presenter
    }

    override fun onBind(comment: Comment?) {
        binding.comment = comment
        binding.executePendingBindings()
    }

}