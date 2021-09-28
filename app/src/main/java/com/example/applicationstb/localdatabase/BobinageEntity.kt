package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.applicationstb.model.Bobinage
import com.example.applicationstb.model.Client
import com.example.applicationstb.model.Section
import java.util.*

@Entity (tableName = "bobinages")
data class BobinageEntity (
    @PrimaryKey var _id:String,
    var numDevis:String?,
    var numFiche:String?,
    var statut:Long?,
    var client:String?,
    var contact:String?,
    var telContact:String?,
    var dateDebut: Date?,
    var dureeTotale:Long?,
    var observation: String?,
    var marqueMoteur:String?,
    var typeBobinage:String?,
    var vitesse: Long?,
    var puissance:Long?,
    var phases:Long?,
    var frequences:Long?,
    var courant:Long?,
    var calageEncoches:Boolean?,
    var sectionFils: MutableList<Section>,
    var nbSpires : Long?,
    var resistanceU : Long?,
    var resistanceV : Long?,
    var resistanceW : Long?,
    var isolementUT : Long?,
    var isolementVT : Long?,
    var isolementWT : Long?,
    var isolementUV : Long?,
    var isolementUW : Long?,
    var isolementVW : Long?,
    var poids : Long?,
    var tension: Long?
    ){
    fun toBobinage() : Bobinage {
        return Bobinage(
            _id,
            numDevis,
            numFiche,
            4,
            statut,
            Client(client!!,null,null,null),
            contact,
            telContact,
            null,
            null,
            dateDebut,
            dureeTotale,
            observation,
            null,
            marqueMoteur,
            typeBobinage,
            puissance,
            vitesse,
            phases,
            frequences,
            courant,
            calageEncoches,
            sectionFils,
            nbSpires,
            resistanceU,
            resistanceV,
            resistanceW,
            isolementUT,
            isolementVT,
            isolementWT,
            isolementUV,
            isolementUW,
            isolementVW,
            null,
            poids,
            tension)
    }

}