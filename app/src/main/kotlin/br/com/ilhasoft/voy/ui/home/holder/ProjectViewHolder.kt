package br.com.ilhasoft.voy.ui.home.holder

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemMapBinding
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.ui.home.HomePresenter

/**
 * Created by developer on 05/12/17.
 */
class ProjectViewHolder(val binding: ItemMapBinding,
                        val presenter: HomePresenter) : ViewHolder<Project>(binding.root) {

    init {
        binding.run {
            isSelected = false
            presenter = this@ProjectViewHolder.presenter
        }
    }

    override fun onBind(project: Project) {
        configProjectSelection(project)
        binding.project = project
        binding.executePendingBindings()
    }

    private fun configProjectSelection(project: Project) {
        binding.isSelected = presenter.getSelectedProject() == project
    }

}