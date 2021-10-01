package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.TypeConverter
import androidx.room.PrimaryKey
import com.example.applicationstb.model.RemontageTriphase

@Entity (tableName = "remontage_triphase")
data class RemontageTriphaseEntity (
    @PrimaryKey var _id:String,
    var numDevis:String?,
    var numFiche: String?,
    var type: Long?,
    var statut: Long?,
    var client: String?,
    var contact: String?,
    var telContact: String?,
    var dureeTotale: Long?,
    var observation: String?,
    var photo: Array<String>?,
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementPorteeFlasque: Int?,
    var verificationFixationCouronne: Boolean?,
    var verificationIsolementPorteBalais: Boolean?,
    var isolementPorteBalaisV: Int?,
    var isolementPorteBalaisOhm: Int?,
    // essais dynamiques
    var tensionStatorInducteurs: Boolean?,
    var tensionStatorInducteursU: Float?,
    var tensionStatorInducteursV: Float?,
    var tensionStatorInducteursW: Float?,
    var intensiteStatorInducteur: Boolean?,
    var intensiteStatorInducteurU: Float?,
    var intensiteStatorInducteurV: Float?,
    var intensiteStatorInducteurW: Float?,
    var tensionInduitRotor: Boolean?,
    var tensionInduitRotorU: Float?,
    var tensionInduitRotorV: Float?,
    var tensionInduitRotorW: Float?,
    var intensiteInduit: Boolean,
    var intensiteInduitU: Float?,
    var vitesseU: Float?,
    var puissanceU: Float?,
    var dureeEssai: Float?,
    var sensRotation: Int?,

//essais vibratoires
    var vitesse1V: Float?,  // vitesse 1v
    var acceleration1V: Float?,  //accélération 1v
    var vitesse2V: Float?,  // vitesse 2v
    var acceleration2V: Float?,  //accélération 2v
    var vitesse1H: Float?,  // vitesse 1H
    var acceleration1H: Float?,  //accélération 1H
    var vitesse2H: Float?,  // vitesse 2H
    var acceleration2H: Float?,  //accélération 2H
    var vitesse2A: Float?,  // vitesse 2A
    var acceleration2A: Float?,  //accélération 2A
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
    var isolementPhaseRotorUW: Float?,
) {
    /*fun toRTriphase(): RemontageTriphase{
        return RemontageTriphase(
        _id,
        numDevis,
        numFiche,
        3,
        statut,
        Client(client,null,null,null),
        contact,
        telContact,
        null,
        null,
        dateDebut,
        dureeTotale,
        observation,
        photo,
        remontageRoulement,
        collageRoulementPorteeArbre,
        collageRoulementPorteeFlasque,
        verificationFixationCouronne,
        verificationIsolementPorteBalais,
        isolementPorteBalaisV,
        isolementPorteBalaisOhm,
        tensionStatorAVide,
        tensionInducAVide,
        intensiteStatorAVide,
        intensiteInducAVide,
        tensionInduitAVide,
        tensionRotorVide,
        tensionSIU,
        tensionSIV,
        tensionSIW,
        intensiteStatorAVide,
        intensiteInducteursAVide,
        intensiteU,
        intensiteV,
        intensiteW,
        tensionInduitAVideU,
        tensionInduitAVideV,
        tensionInduitAVideW,
        tensionRotoOuvertU,
        tensionRotoOuvertV,
        tensionRotoOuvertW,
        tensionIRU,
        tensionIRV,
        tensionIRW,
        intensiteInduit,
        intensiteU,
        vitesseAVide,
        puissanceAVide,
        dureeEssai,
        sensRotation,
//essais vibratoires
        V1V,  // vitesse 1v
        A1V,  //accélération 1v
        V2V,  // vitesse 2v
        A2V,  //accélération 2v
        V1H,  // vitesse 1H
        A1H,  //accélération 1H
        V2H,  // vitesse 2H
        A2H,  //accélération 2H
        V2A,  // vitesse 2A
        A2A,  //accélération 2A
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
         isolementPhaseRotorUW)
    }*/
}