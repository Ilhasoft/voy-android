package br.com.ilhasoft.voy.ui.home.holder

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemNotificationBinding
import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.ui.home.HomePresenter

/**
 * Created by geral on 04/12/17.
 */
class NotificationViewHolder(val binding: ItemNotificationBinding,
                             val presenter: HomePresenter) : ViewHolder<Notification>(binding.root) {

    init {
        binding.presenter = presenter
    }

    override fun onBind(notification: Notification) {
        binding.notification = notification
        binding.executePendingBindings()
    }
}