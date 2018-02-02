package br.com.ilhasoft.voy.ui.home.holder

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.R
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
        binding.notificationTitle.text = getString(getNotificationTitle(notification))
        binding.executePendingBindings()
    }

    private fun getNotificationTitle(notification: Notification): Int {
        return if (notification.status == 1) {
            if (notification.origin == 1)
                R.string.report_was_approved
            else
                R.string.comment_was_approved
        } else {
            if (notification.origin == 1)
                R.string.report_was_not_approved
            else
                R.string.comment_was_not_approved
        }

    }
}