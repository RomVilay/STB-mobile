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
    var resistanceInducteurs: Float?,
    var resistanceInduit: Float?,
    var isolementInducteursMasse: Float?,
    var isolementInduitMasse: Float?,
    var isolementInduitInducteurs: Float?,
    var releveIsoInducteursMasse: Float?,
    var releveIsoInduitMasse: Float?,
    var releveIsoInduitInducteurs: Float?,
){
    fun toRCourantC() : RemontageCourantC {
        return RemontageCourantC(
            _id,
            numDevis!!,
            numFiche!!,
            3,
            statut!!,
            Client(client!!,null,null,null),
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
            tensionStatorInducteurs,
        tensionStatorInducteursU,
        tensionStatorInducteursV,
        tensionStatorInducteursW,
        intensiteStatorInducteur,
        intensiteStatorInducteurU,
        intensiteStatorInducteurV,
        intensiteStatorInducteurW,
        tensionInduitRotor,
        tensionInduitRotorU,
        tensionInduitRotorV,
        tensionInduitRotorW,
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
        acceleration2A,  //accélération 2A
         resistanceInducteurs,
         resistanceInduit,
         isolementInducteursMasse,
         isolementInduitMasse,
         isolementInduitInducteurs,
         releveIsoInducteursMasse,
         releveIsoInduitMasse,
         releveIsoInduitInducteurs
        )
    }
}