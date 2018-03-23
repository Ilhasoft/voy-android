package br.com.ilhasoft.voy

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import java.io.InputStream
import okhttp3.OkHttpClient

/**
 * Created by lucasbarros on 24/11/17.
 */
@GlideModule
class AppGlideModule : com.bumptech.glide.module.AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val factory = OkHttpUrlLoader.Factory(OkHttpClient())
        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setLogLevel(Log.DEBUG)
        super.applyOptions(context, builder)
    }
}