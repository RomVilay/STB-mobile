package com.example.applicationstb.model

import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class Chantier(
    idFiche:String,
    numDevis: String,
    numFiche: String,
    type:Long,
    statut: Long,
    client: Client,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale:Long?,
    observation: String?,
    photo:Array<String>?,
    var vehicule: Vehicule?,
    var adresseChantier:String?,
    var objet:String?,
    var materiel:String?,
    var diagnostic:String?,
    var signatureTech:String?,
    var signatureClient:String?

) : Fiche(idFiche, numDevis, numFiche, type, statut, client, contact, telContact, techniciens, resp, dateDebut, dureeTotale, observation, photo ) {}