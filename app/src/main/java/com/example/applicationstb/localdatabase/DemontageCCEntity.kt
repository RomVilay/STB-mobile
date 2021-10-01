package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Client
//import com.example.applicationstb.model.CourantContinu
import java.util.*

@Entity(tableName = "demontage_cc")
data class DemontageCCEntity (
    @PrimaryKey var _id:String,
    var numDevis:String?,
    var numFiche:String?,
    var statut: Long?,
    var client: String,
    var contact: String?,
    var telContact: String?,
    var dateDebut: Date?,
    var dureeTotale: Long?,
    var observation: String?,
    var photo: Array<String>?,
    var typeFicheDemontage: Int,
    var marque: String?,
    var numSerie: Int?,
    var puissance: Float?,
    var bride: Float?,
    var vitesse : Float?,
    var arbreSortantEntrant:Boolean?, //arbre sortant ou rentrant
    var accouplement:Boolean?,
    var coteAccouplement:String?,
    var clavette: Boolean?,
    var aspect:Int?,
    var aspectInterieur:Int?,
    var couplage:String?,
    var flasqueAvant: Int?,
    var flasqueArriere: Int?,
    var porteeRAvant: Int?,
    var porteeRArriere:  Int?,
    var boutArbre: Boolean?,
    var rondelleElastique: Boolean?,
    var refRoulementAvant: String?,
    var refRoulementArriere: String?,
    var typeRoulementAvant: String ?,
    var typeRoulementArriere: String ?,
    var refJointAvant: String?,
    var refJointArriere: String?,
    var typeJointAvant: Boolean?,
    var typeJointArriere: Boolean?,
    var ventilateur: Int?,
    var capotV: Int?,
    var socleBoiteABorne : Int?,
    var capotBoiteABorne : Int?,
    var plaqueABorne : Int?,
    var presenceSondes: Boolean ?,
    var typeSondes: String?,
    var equilibrage: Boolean?,
    var peinture : String?,
    var isolationMasseInduit: Int?,
    var isolationMassePolesPrincipaux: Int?,
    var isolationMassePolesAuxilliaires: Int?,
    var isolationMassePolesCompensatoires: Int?,
    var isolationMassePorteBalais: Int?,
    var resistanceInduit: Int?,
    var resistancePP: Int?,
    var resistancePA: Int?,
    var resistancePC: Int?,
    /* essais dynamiques */
    var tensionInduit: Int?,
    var intensiteInduit: Int?,
    var tensionExcitation: Int?,
    var intensiteExcitation: Int?
    ){
        /*fun toCContinu (): CourantContinu {
            var fiche = CourantContinu(
                _id,
                numDevis,
                numFiche,
                2,
                statut,
                Client(client,null,null,null),
                contact,
                telContact,
                nu,
                resp,
                dateDebut,
                dureeTotale,
                observation,
                photo,
                typeFicheDemontage,
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
                isolationMasseInduit,
                isolationMassePolesPrincipaux,
                isolationMassePolesAuxilliaires,
                isolationMassePolesCompensatoires,
                isolationMassePorteBalais,
                resistanceInduit,
                resistancePP,
                resistancePA,
                resistancePC,
                /* essais dynamiques */
                tensionInduit,
                intensiteInduit,
                tensionExcitation,
                intensiteExcitation
            )
            return fiche
        }*/
    }