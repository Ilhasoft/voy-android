package br.com.ilhasoft.voy.shared.binding;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import br.com.ilhasoft.voy.GlideApp;

/**
 * Created by developer on 04/12/17.
 */

public class ImageViewBindings {

    private ImageViewBindings() {
        throw new UnsupportedOperationException("This is a pure static class!");
    }

    @BindingAdapter({"app:drawableRes"})
    public static void setImageRes(ImageView imageView, @DrawableRes int imageRes) {
        imageView.setImageResource(imageRes);
    }

    @BindingAdapter(value = {"loadUrl", "placeholder"}, requireAll = false)
    public static void loadFromUrl(ImageView view, String url, Drawable placeholderRes) {
        final Context context = view.getContext();
        GlideApp.with(context)
                .load(url)
                .centerCrop()
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(view);
    }

    public static void loadFromBitmap(ImageView view, Bitmap image, int placeholderRes) {
        final Context context = view.getContext();
        GlideApp.with(context)
                .load(image)
                .centerCrop()
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .dontAnimate()
                .into(view);
    }
}
