package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.TypeConverter
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Client
import com.example.applicationstb.model.RemontageTriphase

@Entity(tableName = "remontage_triphase")
data class RemontageTriphaseEntity(
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
    // essais dynamiques
    var isolementPhaseMasse: Float?,
    var isolementPhase: Float?,
    var resistanceStatorU: Float?,
    var resistanceStatorV: Float?,
    var resistanceStatorW: Float?,
    var isolementPhasePhaseU: Float?,
    var isolementPhasePhaseV: Float?,
    var isolementPhasePhaseW: Float?,
    var vitesseU: Float?,
    var puissanceU: Float?,
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
    var tension:Float?,
    var intensiteU:Float?,
    var intensiteV:Float?,
    var intensiteW:Float?,
    var dureeEssai: Float?
) {
    fun toRTriphase(): RemontageTriphase {
        return RemontageTriphase(
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
            vitesse1V,  // vitesse 1v
            acceleration1V,  //accélération 1v
            vitesse2V,  // vitesse 2v
            acceleration2V,  //accélération 2v
            vitesse1H,  // vitesse 1H
            acceleration1H,  //accélération 1H
            vitesse2H,  // vitesse 2H
            acceleration2H,  //accélération 2H
            vitesse2A,  // vitesse 2A
            acceleration2A,  //accélération 2A
            tension,
            intensiteU,
            intensiteV,
            intensiteW,
            dureeEssai
        )
    }
}