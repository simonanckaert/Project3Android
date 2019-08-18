package com.groep4.mindfulness.model

class Message {
    var content : String = ""
    var gelezen : Boolean = false
    var messageTime : Long = 0
    var messageUser : String = ""

    constructor()

    constructor(content:String, gelezen: Boolean, messageTime : Long, messageUser: String) {
        this.content = content
        this.gelezen = gelezen
        this.messageTime = messageTime
        this.messageUser = messageUser
    }
}