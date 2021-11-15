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
    var status:Long?,
    var client:String?,
    var contact:String?,
    var telContact:String?,
    var dateDebut: Date?,
    var dureeTotale:Long?,
    var observation: String?,
    var photos:Array<String>?,
    var marqueMoteur:String?,
    var typeBobinage:String?,
    var vitesse: Float?,
    var puissance:Float?,
    var phases:Long?,
    var frequences:Float?,
    var courant:Float?,
    var calageEncoches:Boolean?,
    var sectionFils: MutableList<Section>,
    var nbSpires : Long?,
    var resistanceU : Float?,
    var resistanceV : Float?,
    var resistanceW : Float?,
    var isolementUT : Float?,
    var isolementVT : Float?,
    var isolementWT : Float?,
    var isolementUV : Float?,
    var isolementUW : Float?,
    var isolementVW : Float?,
    var poids : Float?,
    var tension: Long?
    ){
    fun toBobinage() : Bobinage {
        return Bobinage(
            _id,
            numDevis,
            numFiche,
            4,
            status,
            Client(client!!,null,null,null, null),
            contact,
            telContact,
            null,
            null,
            dateDebut,
            dureeTotale,
            observation,
            photos,
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