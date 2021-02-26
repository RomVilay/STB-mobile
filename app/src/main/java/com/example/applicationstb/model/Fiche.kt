package com.example.applicationstb.model

open class Fiche(
    val numDevis: String,
    val numFiche: String,
    val client: Client, val contact: String, val telContact: Long, val techniciens: Array<User>, val resp: User) {
}