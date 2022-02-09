package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Client
import com.example.applicationstb.model.RemontageMotoreducteur
import java.util.*

@Entity(tableName = "remontage_motoreducteur")
data class RemontageMotoreducteurEntity(
    @PrimaryKey var _id: String,
    var numDevis: String?,
    var numFiche: String?,
    var statut: Long?,
    var client: String,
    var contact: String?,
    var telContact: String?,
    var dateDebut: Date?,
    var dureeTotale: Long?,
    var observation: String?,
    var photos:Array<String>?,
    var typeFicheRemontage: Int?,
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementPorteeFlasque: Int?,
    var typeMotoreducteur: String?,
    var isolementPhaseMasse: Float?,
    var isolementPhase: Float?,
    var resistanceStatorU: Float?,
    var resistanceStatorV: Float?,
    var resistanceStatorW: Float?,
    var isolementPMStatorU: Float?,
    var isolementPMStatorV: Float?,
    var isolementPMStatorW: Float?,
    var isolementPMRotorU: Float?,
    var isolementPMRotorV: Float?,
    var isolementPMRotorW: Float?,
    var isolementPhaseStatorUV: Float?,
    var isolementPhaseStatorVW: Float?,
    var isolementPhaseStatorUW: Float?,
    var isolementPhaseRotorUV: Float?,
    var isolementPhaseRotorVW: Float?,
    var isolementPhaseRotorUW: Float?
) {
    fun toRemontageMotoreducteur():RemontageMotoreducteur{
        return RemontageMotoreducteur(
            _id,
            numDevis!!,
            numFiche!!,
            3,
            statut!!,
            Client(client,null,null,null, null),
            contact,
            telContact,
            null,
            null,
            null,
            dureeTotale,
            observation,
            photos,
            typeFicheRemontage,
            remontageRoulement,
            collageRoulementPorteeArbre,
            collageRoulementPorteeFlasque,
            typeMotoreducteur,
            isolementPhaseMasse,
            isolementPhase,
            resistanceStatorU,
            resistanceStatorV,
            resistanceStatorW,
            isolementPMStatorU,
            isolementPMStatorV,
            isolementPMStatorW,
            isolementPMRotorU,
            isolementPMRotorV,
            isolementPMRotorW,
            isolementPhaseStatorUV,
            isolementPhaseStatorVW,
            isolementPhaseStatorUW,
            isolementPhaseRotorUV,
            isolementPhaseRotorVW,
            isolementPhaseRotorUW
        )
    }
}
