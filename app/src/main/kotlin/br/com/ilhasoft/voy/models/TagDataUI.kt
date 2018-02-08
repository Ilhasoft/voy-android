package br.com.ilhasoft.voy.models

/**
 * Created by erickjones on 06/02/18.
 */
//TODO find a better way to handle tags mutable properties
data class TagDataUI(var selectedColor: Int = 0,
                     var normalColor: Int = 0,
                     var textSelectedColor: Int = 0,
                     var textNormalColor: Int = 0,
                     var textSize: Float = 0F)

