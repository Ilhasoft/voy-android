package br.com.ilhasoft.voy.ui.report.detail.carousel

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater

import android.view.ViewGroup
import android.view.View
import br.com.ilhasoft.voy.GlideApp

import br.com.ilhasoft.voy.databinding.FragmentCarouselBinding
import br.com.ilhasoft.voy.models.Media
import br.com.ilhasoft.voy.ui.base.BaseFragment

class CarouselFragment : BaseFragment(), CarouselContract {


    companion object {
        @JvmStatic
        fun newInstance(media: Media?): CarouselFragment {
            val args = Bundle()
            args.putParcelable(Media.TAG, media)
            return createWithArguments(args)
        }

        private fun createWithArguments(args: Bundle): CarouselFragment {
            val fragment = CarouselFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var media: Media? = null

    private val binding: FragmentCarouselBinding by lazy {
        FragmentCarouselBinding.inflate(LayoutInflater.from(context))
    }

    private val presenter: CarouselPresenter by lazy { CarouselPresenter() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            binding.root


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        media = arguments.getParcelable(Media.TAG)
        setupView()
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun setupView() {
        binding.run {
            presenter = this@CarouselFragment.presenter
            media?.apply {
                //if (isVideo(uri)) {
                    GlideApp.with(context)
                            .load(uri)
                            .centerCrop()
                            .into(image)
//                    image.visibility = View.GONE
//                    video.visibility = View.VISIBLE
//                    video.setVideoURI(uri)
//                }else{
//                    image.visibility = View.VISIBLE
//                    video.visibility = View.GONE
//                    image.setImageURI(uri)
//                }
            }
        }
    }

    private fun isVideo(uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType != null && mimeType.startsWith("video")
    }

}
