package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.TypeConverter
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Client
import com.example.applicationstb.model.CourantContinu
import com.example.applicationstb.model.RemontageCourantC
import java.util.*

@Entity(tableName = "remontage_cc")
data class RemontageCCEntity (
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
    var tensionStatorAVide: Boolean?,
    var tensionInducAVide: Boolean?,
    var intensiteStatorAVideB: Boolean?,
    var intensiteInducAVideB: Boolean?,
    var tensionInduitAVide: Boolean?,
    var tensionRotorVide: Boolean?,
    var tensionSIU: Float?,
    var tensionSIV: Float?,
    var tensionSIW: Float?,
    var intensiteStatorAVide: Float?,
    var intensiteInducteursAVide: Float?,
    var intensiteU: Float?,
    var intensiteV: Float?,
    var intensiteW: Float?,
    var tensionInduitAVideU: Float?,
    var tensionInduitAVideV: Float?,
    var tensionInduitAVideW: Float?,
    var tensionRotoOuvertU: Float?,
    var tensionRotoOuvertV: Float?,
    var tensionRotoOuvertW: Float?,
    var tensionIRU: Float?,
    var tensionIRV: Float?,
    var tensionIRW: Float?,
    var intensityInduit: Boolean?,
    var intensityU: Float?,
    var vitesseAVide: Float?,
    var puissanceAVide: Float?,
    var dureeEssai: Float?,
    var sensRotation: Int?,
    var V1V: Float?,  // vitesse 1v
    var A1V: Float?,  //accélération 1v
    var V2V: Float?,  // vitesse 2v
    var A2V: Float?,  //accélération 2v
    var V1H: Float?,  // vitesse 1H
    var A1H: Float?,  //accélération 1H
    var V2H: Float?,  // vitesse 2H
    var A2H: Float?,  //accélération 2H
    var V2A: Float?,  // vitesse 2A
    var A2A: Float?,  //accélération 2A
    var resistanceInducteurs: Float?,
    var resistanceInduit: Float?,
    var isolementInducteursMasse: Float?,
    var isolementInduitMasse: Float?,
    var isolementInduitInducteurs: Float?,
    var releveIsoInducteursMasse: Float?,
    var releveIsoInduitMasse: Float?,
    var releveIsoInduitInducteurs: Float?
){
    /*fun toRCourantC() : RemontageCourantC {
        return RemontageCourantC(
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
         resistanceInducteurs,
         resistanceInduit,
         isolementInducteursMasse,
         isolementInduitMasse,
         isolementInduitInducteurs,
         releveIsoInducteursMasse,
         releveIsoInduitMasse,
         releveIsoInduitInducteurs
        )
    }*/
}