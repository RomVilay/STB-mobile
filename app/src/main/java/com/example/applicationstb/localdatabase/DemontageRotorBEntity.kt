package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Client
import com.example.applicationstb.model.DemontageRotorBobine
import com.example.applicationstb.model.Triphase
import java.util.*

@Entity(tableName = "demontage_rotorb")
data class DemontageRotorBEntity(
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
    var typeFicheDemontage: Int,
    var marque: String?,
    var numSerie: Int?,
    var puissance: Float?,
    var bride: Float?,
    var vitesse: Float?,
    var arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    var accouplement: Boolean?,
    var coteAccouplement: String?,
    var clavette: Boolean?,
    var aspect: Int?,
    var aspectInterieur: Int?,
    var couplage: String?,
    var flasqueAvant: Int?,
    var flasqueArriere: Int?,
    var porteeRAvant: Int?,
    var porteeRArriere: Int?,
    var boutArbre: Boolean?,
    var rondelleElastique: Boolean?,
    var refRoulementAvant: String?,
    var refRoulementArriere: String?,
    var typeRoulementAvant: String?,
    var typeRoulementArriere: String?,
    var refJointAvant: String?,
    var refJointArriere: String?,
    var typeJointAvant: Boolean?,
    var typeJointArriere: Boolean?,
    var ventilateur: Int?,
    var capotV: Int?,
    var socleBoiteABorne: Int?,
    var capotBoiteABorne: Int?,
    var plaqueABorne: Int?,
    var presenceSondes: Boolean?,
    var typeSondes: String?,
    var equilibrage: Boolean?,
    var peinture: String?,
    var isolementPhaseMasseStatorUM	: Float?,
    var isolementPhaseMasseStatorVM	: Float?,
    var isolementPhaseMasseStatorWM	: Float?,
    var isolementPhaseMasseRotorB1M	: Float?,
    var isolementPhaseMasseRotorB2M	: Float?,
    var isolementPhaseMasseRotorB3M	: Float?,
    var isolementPhaseMassePorteBalaisM	: Float?,
    var isolementPhasePhaseStatorUV	: Float?,
    var isolementPhasePhaseStatorVW	: Float?,
    var isolementPhasePhaseStatorUW	: Float?,
    var resistanceStatorU	: Float?,
    var resistanceStatorV	: Float?,
    var resistanceStatorW	: Float?,
    var resistanceRotorB1B2	: Float?,
    var resistanceRotorB2B2	: Float?,
    var resistanceRotorB1B3	: Float?,
    var tensionU	: Float?,
    var tensionV	: Float?,
    var tensionW	: Float?,
    var tensionRotor	: Float?,
    var intensiteU	: Float?,
    var intensiteV	: Float?,
    var intensiteW	: Float?,
    var intensiteRotor	: Float?,
    var dureeEssai	: Int?
) {
    fun toDemoRotorB(): DemontageRotorBobine {
        var fiche = DemontageRotorBobine(
            _id,
            numDevis,
            numFiche,
            2,
            statut,
            Client(client,null,null,null),
            contact,
            telContact,
            null,
            null,
            dateDebut,
            dureeTotale,
            observation,
            null,
            marque,
            numSerie,
            puissance,
            bride,
            vitesse,
            arbreSortantEntrant,
            accouplement,
            coteAccouplement,
            clavette,
            aspect,
            aspectInterieur,
            couplage,
            flasqueAvant,
            flasqueArriere,
            porteeRArriere,
            porteeRAvant,
            boutArbre,
            rondelleElastique,
            refRoulementAvant,
            refRoulementArriere,
            typeRoulementAvant,
            typeRoulementArriere,
            refJointAvant,
            refJointArriere,
            typeJointAvant,
            typeJointArriere,
            ventilateur,
            capotV,
            socleBoiteABorne,
            capotBoiteABorne,
            plaqueABorne,
            presenceSondes,
            typeSondes,
            equilibrage,
            peinture,
            isolementPhaseMasseStatorUM	,
        isolementPhaseMasseStatorVM	,
        isolementPhaseMasseStatorWM	,
        isolementPhaseMasseRotorB1M	,
        isolementPhaseMasseRotorB2M	,
        isolementPhaseMasseRotorB3M	,
        isolementPhaseMassePorteBalaisM	,
        isolementPhasePhaseStatorUV	,
        isolementPhasePhaseStatorVW	,
        isolementPhasePhaseStatorUW	,
        resistanceStatorU	,
        resistanceStatorV	,
        resistanceStatorW	,
        resistanceRotorB1B2	,
        resistanceRotorB2B2	,
        resistanceRotorB1B3	,
        tensionU	,
        tensionV	,
        tensionW	,
        tensionRotor	,
        intensiteU	,
        intensiteV	,
        intensiteW	,
        intensiteRotor	,
        dureeEssai)
        return fiche
    }
}