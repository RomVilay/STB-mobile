package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Client
import com.example.applicationstb.model.DemontageMonophase
import com.example.applicationstb.model.Triphase
import java.util.*

@Entity(tableName = "demontage_monophase")
data class DemontageMonophaseEntity(
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
    var photo:Array<String>?,
    var typeFicheDemontage: Int,
    var typeMoteur: String?,
    var marque: String?,
    var numSerie: String?,
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
    var refRoulementAvant: Array<String>?,
    var refRoulementArriere: Array<String>?,
    var typeRoulementAvant: Array<String>?,
    var typeRoulementArriere: Array<String>?,
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
    var isolementPhaseMasse: Float?,
    var resistanceTravail	: Float?,
    var resistanceDemarrage	: Float?,
    var valeurCondensateur	: Float?,
    var tension	: Float?,
    var intensite	: Float?
) {
    fun toMonophase(): DemontageMonophase {
        var fiche = DemontageMonophase(
            _id,
            numDevis,
            numFiche,
            2,
            statut,
            Client(client,null,null,null, null),
            contact,
            telContact,
            null,
            null,
            dateDebut,
            dureeTotale,
            observation,
            photo,
            typeMoteur,
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
            isolementPhaseMasse,
        resistanceTravail	,
        resistanceDemarrage	,
        valeurCondensateur	,
        tension	,
        intensite)
        return fiche
    }
}