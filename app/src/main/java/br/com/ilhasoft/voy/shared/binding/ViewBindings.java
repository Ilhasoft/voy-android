package br.com.ilhasoft.voy.shared.binding;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * Created by felipe on 31/01/18.
 */

public final class ViewBindings {

    private ViewBindings() {}

    @BindingAdapter("isVisible")
    public static void isVisible(View view, boolean predicate) {
        view.setVisibility(predicate ? View.VISIBLE : View.GONE);
    }
}
