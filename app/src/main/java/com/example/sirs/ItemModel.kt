package com.example.sirs

import java.io.Serializable

class ItemModel : Serializable {
    var idTempat: String? = ""
    var namaTempat: String? = ""
    var alamatTempat: String? = ""
    var gambarTempat: String? = ""
    var latitude: Double? = null
    var longitude: Double? = null
    var category:String? = ""
}