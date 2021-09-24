package com.example.applicationstb.model

import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class Chantier(
    idFiche:String,
    numDevis: String?,
    numFiche: String?,
    type:Long?,
    statut: Long?,
    client: Client?,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale:Long?,
    observations: String?,
    photo:Array<String>?,
    var vehicule: String?,
    var adresseChantier:String?,
    var objet:String?,
    var materiel:String?,
    var diagnostic:String?,
    var signatureTech:String?,
    var signatureClient:String?

) : Fiche(idFiche, numDevis, numFiche, type, statut, client, contact, telContact, techniciens, resp, dateDebut, dureeTotale, observations, photo ) {
    override fun toString(): String {
        return "{\"status\": ${status}," +
                "\" dureeTotale\": ${dureeTotale},"+
            "\"photos \": ${photo},"+
            "\" _id \": ${_id},"+
            "\" numDevis\": ${numDevis},"+
            "\" numFiche\": ${numFiche},"+
            "\" type\": ${type},"+
            "\" client\": ${client},"+
            "\" resp\": ${resp},"+
            "\" contact\": ${contact},"+
            "\" telContact\": ${telContact},"+
            "\" observations\": ${observations},"+
            "\" dateDebut\": ${dateDebut},"+
            "\" techniciens \": ${techniciens},"+
            "\" signatureClient \": ${signatureClient},"+
            "\" vehicule \": ${vehicule},"+
            "\" adresseChantier \": ${adresseChantier},"+
            "\" materiel \": ${materiel},"+
            "\" objet\": ${objet} }"
        }
}