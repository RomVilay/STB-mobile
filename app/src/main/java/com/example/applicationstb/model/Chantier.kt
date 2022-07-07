package com.example.applicationstb.model

import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applicationstb.localdatabase.BobinageEntity
import com.example.applicationstb.localdatabase.ChantierEntity
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
    resp: User?,
    contact: String?,
    telContact: String?,
    dateDebut: Date?,
    dureeTotale:Long?,
    observations: String?,
    photos:Array<String>?,
    techniciens: Array<String>?,
    var adresseChantier:String?,
    var vehicule: String?,
    var objet:String?,
    var materiel:String?,
    var diagnostic:String?,
    var signatureTech:String?,
    var signatureClient:String?,
    var dureeEssai:String?

) : Fiche(idFiche, numDevis, numFiche, type, statut, client, resp,contact, telContact, dateDebut, dureeTotale, observations, photos, techniciens ) {
    fun toEntity() : ChantierEntity {
        return ChantierEntity(
            _id,
            numDevis,
            numFiche,
            status!!,
            client!!._id,
            contact,
            telContact,
            dateDebut,
            dureeTotale!!,
            observations,
            photos,
            vehicule,
            adresseChantier,
            objet,
            materiel,
            diagnostic,
            signatureTech,
            signatureClient,
            dureeEssai
        )
    }
}