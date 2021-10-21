package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Autre
import com.example.applicationstb.model.Client

@Entity(tableName = "remontage")
data class RemontageEntity (
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
    var typeFicheRemontage: Int?,
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementPorteeFlasque: Int?,
    var verificationFixationCouronne: Boolean?,
    var verificationIsolementPorteBalais: Boolean?,
    var isolementPorteBalaisV: Int?,
    var isolementPorteBalaisOhm: Int?
){
    fun toRemo(): Autre{
        return Autre(
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
            typeFicheRemontage,
            remontageRoulement,
            collageRoulementPorteeArbre,
            collageRoulementPorteeFlasque,
            verificationFixationCouronne,
            verificationIsolementPorteBalais,
            isolementPorteBalaisV,
            isolementPorteBalaisOhm,
        )
    }
}