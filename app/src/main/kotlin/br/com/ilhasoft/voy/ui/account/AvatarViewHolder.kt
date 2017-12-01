package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ViewItemAvatarBinding
import br.com.ilhasoft.voy.models.Avatar

/**
 * Created by geral on 01/12/17.
 */
class AvatarViewHolder(
        val binding: ViewItemAvatarBinding,
        val presenter: AccountPresenter): ViewHolder<Avatar>(binding.root){

    override fun onBind(avatar: Avatar?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}