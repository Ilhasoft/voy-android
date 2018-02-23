package br.com.ilhasoft.voy.db.theme

import br.com.ilhasoft.voy.models.Theme
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by lucasbarros on 08/02/18.
 */
open class ThemeDbModel : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var projectId: Int = 0
    var name: String = "ThemeName"
    var bounds: RealmList<BoundDbModel> = RealmList()
    var tags: RealmList<String> = RealmList()
    var color: String = "00cbff"
    var allowLinks: Boolean = true
}

fun ThemeDbModel.toTheme(): Theme {
    return Theme(id = this.id,
            name = this.name,
            bounds = this.bounds.map { arrayListOf(it.lat, it.lng) }.toList(),
            tags = this.tags,
            color = this.color,
            allowLinks = this.allowLinks)
}
