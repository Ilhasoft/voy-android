package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemAvatarBinding

/**
 * Created by geral on 01/12/17.
 */
class AvatarViewHolder(val binding: ItemAvatarBinding,
                       val presenter: AccountPresenter) : ViewHolder<Int>(binding.root) {

    override fun onBind(avatar: Int?) {
        binding.drawableId = avatar
    }

}