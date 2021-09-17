package com.example.applicationstb.model

import java.util.*

open class Fiche(
    val id:String,
    val numDevis: String,
    val numFiche: String,
    val type: Long,
    val status: Long,
    val client: Client,
    val contact: String?,
    val telContact: String?,
    val techniciens: Array<User>?,
    val resp: User?,
    val dateDebut: Date?,
    val dureeTotale: Long?,
    val observation: String?,
    val photo: Array<String>?) {
}