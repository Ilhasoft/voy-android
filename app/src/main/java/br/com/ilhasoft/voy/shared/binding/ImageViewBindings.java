package br.com.ilhasoft.voy.shared.binding;

import android.databinding.BindingAdapter;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

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

}
