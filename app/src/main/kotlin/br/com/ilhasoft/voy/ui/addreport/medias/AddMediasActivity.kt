package br.com.ilhasoft.voy.ui.addreport.medias

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import br.com.ilhasoft.support.media.MediaSelectorDelegate
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityAddMediasBinding
import br.com.ilhasoft.voy.shared.widget.AddImageView
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.shared.OnAddImageClickListener

/**
 * Created by lucasbarros on 23/11/17.
 */
class AddMediasActivity : BaseActivity(), AddMediasContract,
        OnAddImageClickListener {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityAddMediasBinding>(this, R.layout.activity_add_medias)
    }

    private val presenter by lazy { AddMediasPresenter() }

    private val delegate: MediaSelectorDelegate by lazy {
        val listener = MediaSelectorDelegate.OnLoadMediaListener {
            it?.let { onNewPhoto(it) }
        }
        MediaSelectorDelegate(this, "br.com.ilhasoft.voy.provider")
                .setOnLoadImageListener(listener)
                .setOnLoadGalleryImageListener(listener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupToolbar()
        setUpImages()
        presenter.attachView(this)
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

    override fun getMedia() {
        delegate.selectMedia(this, MediaSelectorDelegate.GALLERY_IMAGE or MediaSelectorDelegate.CAMERA_IMAGE)
    }

    private var imageViewSelected: AddImageView? = null

    override fun onClickAddImage(addImageView: AddImageView) {
        imageViewSelected = addImageView
        getMedia()
    }

    override fun onClickRemove(imageView: ImageView) {

    }

    private fun setupView() {
        binding.run {
            presenter = this@AddMediasActivity.presenter
        }
    }

    private fun setupToolbar() {
        binding.toolbar?.run {
            presenter = this@AddMediasActivity.presenter
        }
    }

    private fun setUpImages() = with(binding) {
        image1.setImageListener(this@AddMediasActivity)
        image2.setImageListener(this@AddMediasActivity)
        image3.setImageListener(this@AddMediasActivity)
        image4.setImageListener(this@AddMediasActivity)
    }

    private fun onNewPhoto(uri: Uri) {
        imageViewSelected?.setImageFromUri(uri)

    }
}