package br.com.ilhasoft.voy.ui.account

import android.support.v4.content.ContextCompat
import android.view.View
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ItemAvatarBinding

/**
 * Created by geral on 01/12/17.
 */
class AvatarViewHolder(val binding: ItemAvatarBinding,
                       val presenter: AccountPresenter) :
        ViewHolder<Int>(binding.root), View.OnClickListener {

    init {
        binding.let {
            it.clicked = false
            it.apply {
                circleImageView.borderWidth = context.resources.getDimension(R.dimen.avatar_no_border).toInt()
                circleImageView.borderColor = ContextCompat.getColor(context, android.R.color.transparent)
                circleImageView.setOnClickListener(this@AvatarViewHolder)
            }
        }
    }

    override fun onBind(avatar: Int?) {
        configAvatarSelection(avatar)
        binding.drawableId = avatar
        binding.executePendingBindings()
    }

    override fun onClick(v: View?) {
        presenter.setSelectedAvatar(binding.drawableId)
    }

    private fun configAvatarSelection(avatar: Int?) {
        binding.clicked = presenter.getSelectedAvatar() == avatar
        binding.clicked.let {
            binding.circleImageView.borderWidth = setupBorderWidth(it)
            binding.circleImageView.borderColor = setupBorderColor(it)
        }
    }

    private fun setupBorderWidth(viewClicked: Boolean?): Int {
        return viewClicked?.let {
            if (it) context.resources.getDimension(R.dimen.avatar_with_border).toInt()
            else context.resources.getDimension(R.dimen.avatar_no_border).toInt()
        } ?: context.resources.getDimension(R.dimen.avatar_no_border).toInt()
    }

    private fun setupBorderColor(viewClicked: Boolean?): Int {
        return viewClicked?.let {
            if (it) ContextCompat.getColor(context, R.color.bright_sky_blue)
            else ContextCompat.getColor(context, android.R.color.transparent)
        } ?: ContextCompat.getColor(context, android.R.color.transparent)
    }

}