package com.example.applicationstb.model

import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class Chantier(
    numDevis: String,
    numChantier: String,
    client: Client,
    contact: String,
    telContact: Long,
    techniciens: Array<User>,
    resp: User,
    var vehicule: Vehicule,
    var adresse:String,
    val objet:String,
    val materiel:String,
    val diagnostic:String,
    val observations:String

) : Fiche(numDevis, numChantier, client, contact, telContact, techniciens, resp) {
    lateinit var DateDebut: LocalDateTime;
    lateinit var DateFin: LocalDateTime;
    lateinit var SignatureClient:String;
    lateinit var SignatureTech:String;
    init {
        DateDebut = LocalDateTime.now()
        DateFin = LocalDateTime.now()
        SignatureClient = "./source"
        SignatureTech = "./source"
    }
    fun setSC(s:String){
        this.SignatureClient = s
    }
    fun setST(s:String){
        this.SignatureTech = s
    }
}