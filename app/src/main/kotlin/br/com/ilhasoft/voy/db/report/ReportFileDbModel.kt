package br.com.ilhasoft.voy.db.report

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by lucasbarros on 21/02/18.
 */
open class ReportFileDbModel : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var file: String = ""
}