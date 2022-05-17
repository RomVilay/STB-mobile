package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.TypeConverter
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Client
import com.example.applicationstb.model.CourantContinu
import com.example.applicationstb.model.RemontageCourantC
import java.util.*

@Entity(tableName = "remontage_cc")
data class RemontageCCEntity(
    @PrimaryKey var _id: String,
    var numDevis: String?,
    var numFiche: String?,
    var type: Long?,
    var statut: Long?,
    var client: String,
    var contact: String?,
    var telContact: String?,
    var dureeTotale: Long?,
    var observation: String?,
    var photos: Array<String>?,
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementPorteeFlasque: Int?,
    var isolementPhaseMasse: String?,
    var isolementPhase: String?,
    var resistanceStatorU: String?,
    var resistanceStatorV: String?,
    var resistanceStatorW: String?,
    var isolementPhasePhaseU: String?,
    var isolementPhasePhaseV: String?,
    var isolementPhasePhaseW: String?,
    var vitesseU: String?,
    var puissanceU: String?,
    var sensRotation: String?,
    //champs spécifiques
    var verificationFixationCouronne: Boolean?,
    var isolementPhaseMasseRotorU:String?,
    var isolementPhaseMasseRotorV:String?,
    var isolementPhaseMasseRotorW:String?,
    var isolementPhaseRotorUV:String?,
    var isolementPhaseRotorVW:String?,
    var isolementPhaseRotorUW:String?,
    var tensionInducteursU:String?,
    var tensionInducteursV:String?,
    var tensionInducteursW:String?,
    var intensitéInducteursU:String?,
    var intensitéInducteursV:String?,
    var intensitéInducteursW:String?,
    var tensionInduitU : String?,
    var tensionInduitV : String?,
    var tensionInduitW : String?,
    var tensionRotorU : String?,
    var tensionRotorV : String?,
    var tensionRotorW : String?,
    var tensionUExcitation : String?,
    var intensitéUExcitation : String?,
    var isolementInduit: String?,
    var isolementPolePrincipal: Float?,
    var isolementPoleAuxilliaire:Float?,
    var isolementPoleCompensatoire:Float?,
    var isolementBoiteBalais:Float?,
    var resistanceInduit:Float?,
    var resistancePolePrincipal:Float?,
    var resistancePoleAuxilliaire:Float?,
    var resistancePoleCompensatoire:Float?,
    var intensiteInduit:Float?,
    // essais dynamiques

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
    var acceleration2A: Float?  //accélération 2
) {
    fun toRCourantC(): RemontageCourantC {
        return RemontageCourantC(
            _id,
            numDevis!!,
            numFiche!!,
            3,
            statut!!,
            Client(client, null, null, null, null),
            contact,
            telContact,
            null,
            null,
            null,
            dureeTotale,
            observation,
            photos,
            remontageRoulement,
            collageRoulementPorteeArbre,
            collageRoulementPorteeFlasque,
            isolementPhaseMasse,
            isolementPhase,
            resistanceStatorU,
            resistanceStatorV,
            resistanceStatorW,
            isolementPhasePhaseU,
            isolementPhasePhaseV,
            isolementPhasePhaseW,
            vitesseU,
            puissanceU,
            sensRotation,
            verificationFixationCouronne,
            isolementPhaseMasseRotorU,
            isolementPhaseMasseRotorV,
            isolementPhaseMasseRotorW,
            isolementPhaseRotorUV,
            isolementPhaseRotorVW,
            isolementPhaseRotorUW,
            tensionInducteursU,
            tensionInducteursV,
            tensionInducteursW,
            intensitéInducteursU,
            intensitéInducteursV,
            intensitéInducteursW,
            tensionInduitU,
            tensionInduitV,
            tensionInduitW,
            tensionRotorU,
            tensionRotorV,
            tensionRotorW,
            tensionUExcitation,
            intensitéUExcitation,
            isolementInduit,
            isolementPolePrincipal,
            isolementPoleAuxilliaire,
            isolementPoleCompensatoire,
            isolementBoiteBalais,
            resistanceInduit,
            resistancePolePrincipal,
            resistancePoleAuxilliaire,
            resistancePoleCompensatoire,
            intensiteInduit,
            // essais dynamiques
            vitesse1V,  // vitesse 1v
            acceleration1V,  //accélération 1v
            vitesse2V,  // vitesse 2v
            acceleration2V,  //accélération 2v
            vitesse1H,  // vitesse 1H
            acceleration1H,  //accélération 1H
            vitesse2H,  // vitesse 2H
            acceleration2H,  //accélération 2H
            vitesse2A,  // vitesse 2A
            acceleration2A  //accélération 2A
        )
    }
}