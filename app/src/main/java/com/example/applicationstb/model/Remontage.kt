package com.example.applicationstb.model

import com.example.applicationstb.localdatabase.*
import java.sql.Timestamp
import java.util.*

open class Remontage(
    idFiche: String,
    numDevis: String,
    numFiche: String,
    type: Long,
    statut: Long,
    client: Client,
    contact: String?,
    telContact: String?,
    techniciens: Array<String>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale: Long?,
    observations: String?,
    photos: Array<String>?,
    open var typeFicheRemontage: Int?,
    open var remontageRoulement: Int?,
    open var collageRoulementPorteeArbre: Int?,
    open var collageRoulementFlasque: Int?,
) : Fiche(
    idFiche, numDevis, numFiche, type, statut, client, resp,contact, telContact, dateDebut, dureeTotale, observations, photos, techniciens
) {}