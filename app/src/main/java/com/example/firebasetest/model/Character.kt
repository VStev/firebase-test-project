package com.example.firebasetest.model

data class Character(
    val charName: String,
    val charRace: String,
    val charClass: String,
    val charNational: String,
    val detail: String,
    val owner: String
){
    constructor(): this(
        "Kasumi",
        "Human Ancient",
        "Miko",
        "Orzecan",
        "Default Character Miko",
        "10879795"
    )
}