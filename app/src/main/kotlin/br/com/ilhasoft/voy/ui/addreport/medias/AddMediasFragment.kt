package br.com.ilhasoft.voy.ui.addreport.medias

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ilhasoft.support.media.MediaSelectorDelegate
import br.com.ilhasoft.voy.databinding.FragmentAddMediasBinding
import br.com.ilhasoft.voy.shared.widget.AddImageView
import br.com.ilhasoft.voy.ui.addreport.ReportViewModel
import br.com.ilhasoft.voy.ui.base.BaseFragment
import br.com.ilhasoft.voy.ui.shared.OnAddImageClickListener

class AddMediasFragment :
        BaseFragment(),
        OnAddImageClickListener {

    private val binding: FragmentAddMediasBinding by lazy {
        FragmentAddMediasBinding.inflate(LayoutInflater.from(context))
    }

    private val reportViewModel: ReportViewModel by lazy {
        ViewModelProviders.of(activity).get(ReportViewModel::class.java)
    }

    private val delegate: MediaSelectorDelegate by lazy {
        val listener = MediaSelectorDelegate.OnLoadMediaListener {
            it?.let { onNewPhoto(it) }
        }
        MediaSelectorDelegate(context, "br.com.ilhasoft.voy.provider")
                .setOnLoadImageListener(listener)
                .setOnLoadVideoListener(listener)
    }
    private var imageViewSelected: AddImageView? = null

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        reportViewModel.setButtonEnable(reportViewModel.medias.isNotEmpty())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        delegate.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        delegate.onRequestPermissionResult(this, requestCode, grantResults)
    }

    override fun onClickAddImage(addImageView: AddImageView) {
        imageViewSelected = addImageView
        getMedia()
    }

    override fun onClickRemove(uri: Uri) {
        reportViewModel.removeUri(uri)
    }

    private fun getMedia() {
        delegate.selectMedia(this, MediaSelectorDelegate.CAMERA_IMAGE
                or MediaSelectorDelegate.VIDEO)
    }

    private fun insertMedia(position: Int, imagePath: Uri) = with(binding) {
        val imageView = when (position) {
            0 -> image1
            1 -> image2
            2 -> image3
            else -> image4
        }
        imageView.setMediaFromUri(imagePath)
    }

    private fun setupView() = with(binding) {
        image1.setImageListener(this@AddMediasFragment)
        image2.setImageListener(this@AddMediasFragment)
        image3.setImageListener(this@AddMediasFragment)
        image4.setImageListener(this@AddMediasFragment)

        for (index in reportViewModel.medias.indices) {
            insertMedia(index, reportViewModel.medias[index])
        }
    }

    private fun onNewPhoto(uri: Uri) {
        imageViewSelected?.setMediaFromUri(uri)
        reportViewModel.addUri(uri)
    }

}
