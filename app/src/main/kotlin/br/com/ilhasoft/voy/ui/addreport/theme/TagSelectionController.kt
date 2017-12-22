package br.com.ilhasoft.voy.ui.addreport.theme

import android.databinding.BaseObservable
import android.databinding.Bindable
import br.com.ilhasoft.voy.models.Tag
import br.com.ilhasoft.support.databinding.BR

/**
 * Created by geral on 22/12/17.
 */
class TagSelectionController(clicked: Boolean = false): BaseObservable() {

    @Bindable
    var clicked = clicked
    set(value) {
        if(value != field) {
            field = value
            notifyPropertyChanged(BR.clicked)
        }
    }

}