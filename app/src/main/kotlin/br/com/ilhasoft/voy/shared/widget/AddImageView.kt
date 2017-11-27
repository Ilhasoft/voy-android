package br.com.ilhasoft.voy.shared.widget

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import br.com.ilhasoft.voy.GlideApp
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ViewAddImageBinding
import br.com.ilhasoft.voy.ui.shared.OnAddImageClickListener

/**
 * Created by lucasbarros on 24/11/17.
 */
class AddImageView : FrameLayout {

    private var binding: ViewAddImageBinding? = null
    private var listener: OnAddImageClickListener? = null

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        val layoutInflater = LayoutInflater.from(context)
        binding = ViewAddImageBinding.inflate(layoutInflater, this, true)
        binding?.apply {
            image.setOnClickListener { listener?.onClickAddImage(this@AddImageView) }
            remove.setOnClickListener {
                removeImage()
                listener?.onClickRemove(image)
            }
        }
    }

    private fun removeImage() {
        binding?.apply {
            image.setImageResource(R.drawable.ic_add)
            remove.visibility = View.GONE
        }
    }

    fun setImageFromUri(uri: Uri) {
        binding?.apply {
            GlideApp.with(context)
                    .load(uri)
                    .centerCrop()
                    .into(image)
            remove.visibility = View.VISIBLE
        }
    }

    fun setImageListener(listener: OnAddImageClickListener) {
        this@AddImageView.listener = listener
    }
}