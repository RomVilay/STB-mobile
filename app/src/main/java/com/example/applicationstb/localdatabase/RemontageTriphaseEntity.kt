package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.TypeConverter
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Client
import com.example.applicationstb.model.RemontageTriphase

@Entity (tableName = "remontage_triphase")
data class RemontageTriphaseEntity (
    @PrimaryKey var _id:String,
    var numDevis:String?,
    var numFiche: String?,
    var type: Long?,
    var statut: Long?,
    var client: String,
    var contact: String?,
    var telContact: String?,
    var dureeTotale: Long?,
    var observation: String?,
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementPorteeFlasque: Int?,
    var verificationFixationCouronne: Boolean?,
    var verificationIsolementPorteBalais: Boolean?,
    var isolementPorteBalaisV: Int?,
    var isolementPorteBalaisOhm: Int?,
    // essais dynamiques
     var tensionStator:Boolean?,
     var tensionStatorU:Float?,
     var tensionStatorV:Float?,
     var tensionStatorW:Float?,
     var tensionInducteurs: Boolean?,
     var tensionInducteursU: Float?,
     var tensionInducteursV: Float?,
     var tensionInducteursW: Float?,
     var intensiteStator:Boolean?,
     var intensiteStatorU:Float?,
     var intensiteStatorV:Float?,
     var intensiteStatorW:Float?,
     var intensiteInducteurs: Boolean?,
     var intensiteInducteursU: Float?,
     var intensiteInducteursV: Float?,
     var intensiteInducteursW: Float?,
     var tensionInduit:Boolean?,
     var tensionInduitU:Float?,
     var tensionInduitV:Float?,
     var tensionInduitW:Float?,
     var tensionRotor: Boolean?,
     var tensionRotorU: Float?,
     var tensionRotorV: Float?,
     var tensionRotorW: Float?,
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
    fun toRTriphase(): RemontageTriphase{
        return RemontageTriphase(
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
        null,
            remontageRoulement,
            collageRoulementPorteeArbre,
            collageRoulementPorteeFlasque,
            verificationFixationCouronne,
            verificationIsolementPorteBalais,
            isolementPorteBalaisV,
            isolementPorteBalaisOhm,
            tensionStator,
            tensionStatorU,
            tensionStatorV,
            tensionStatorW,
            tensionInducteurs,
            tensionInducteursU,
            tensionInducteursV,
            tensionInducteursW,
            intensiteStator,
            intensiteStatorU,
            intensiteStatorV,
            intensiteStatorW,
            intensiteInducteurs,
            intensiteInducteursU,
            intensiteInducteursV,
            intensiteInducteursW,
            tensionInduit,
            tensionInduitU,
            tensionInduitV,
            tensionInduitW,
            tensionRotor,
            tensionRotorU,
            tensionRotorV,
            tensionRotorW,
            intensiteInduit,
            intensiteInduitU,
            vitesseU,
            puissanceU,
            dureeEssai,
            sensRotation,
//essais vibratoires
            vitesse1V,  // vitesse 1v
            acceleration1V,  //accélération 1v
            vitesse2V,  // vitesse 2v
            acceleration2V,  //accélération 2v
            vitesse1H,  // vitesse 1H
            acceleration1H,  //accélération 1H
            vitesse2H,  // vitesse 2H
            acceleration2H,  //accélération 2H
            vitesse2A,  // vitesse 2acceleration
            acceleration2A,
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
    }
}