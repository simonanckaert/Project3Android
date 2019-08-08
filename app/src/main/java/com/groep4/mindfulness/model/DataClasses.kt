package com.groep4.mindfulness.model

data class Task(

        var _date : Long,
        var _group: String,
        var _key: String,
        var _text: String
) {
    constructor() : this(-1,"","","")
}

data class User(val name: String, val email: String, val groepnr: Number, val uid: String, val telnr: String, val regio: String)
