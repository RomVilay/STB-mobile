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
    var verificationFixationCouronne: Boolean?,
    var verificationIsolementPorteBalais: Boolean?,
    var isolementPorteBalaisV: Float?,
    var isolementPorteBalaisOhm: Float?,
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
    var acceleration2A: Float?,  //accélération 2A
    var isolementPolePrincipal: Float?,
    var isolementPoleAuxilliaire: Float?,
    var isolementPoleCompensatoire: Float?,
    var isolementBoiteBalais: Float?,
    var resistanceInduit: Float?,
    var resistancePolePrincipal: Float?,
    var resistancePoleAuxilliaire: Float?,
    var resistancePoleCompensatoire: Float?,
    var tensionInduit: Float?,
    var tensionExcit: Float?,
    var intensiteInduit: Float?,
    var intensiteExcit: Float?
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
            verificationFixationCouronne,
            verificationIsolementPorteBalais,
            isolementPorteBalaisV,
            isolementPorteBalaisOhm,
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
            isolementPolePrincipal,
            isolementPoleAuxilliaire,
            isolementPoleCompensatoire,
            isolementBoiteBalais,
            resistanceInduit,
            resistancePolePrincipal,
            resistancePoleAuxilliaire,
            resistancePoleCompensatoire,
            tensionInduit,
            tensionExcit,
            intensiteInduit,
            intensiteExcit
        )
    }
}