package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemNotificationBinding
import br.com.ilhasoft.voy.models.Notification

/**
 * Created by geral on 04/12/17.
 */
class NotificationViewHolder(val binding: ItemNotificationBinding,
                             presenter: HomePresenter) : ViewHolder<Notification>(binding.root) {

    init {
        binding.presenter = presenter
    }

    override fun onBind(notification: Notification) {
        binding.notificationTitle.text = notification.title
        binding.notification = notification
    }
}