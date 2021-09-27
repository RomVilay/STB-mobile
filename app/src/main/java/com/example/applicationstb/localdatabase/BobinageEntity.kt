package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Bobinage
import com.example.applicationstb.model.Section

@Entity (tableName = "bobinages")
data class BobinageEntity (
    @PrimaryKey var _id:String,
    var marqueMoteur:String?,
    var typeBobinage:String?,
    var vitesse: Long?,
    var puissance:Long?,
    var phases:Long?,
    var frequences:Long?,
    var courant:Long?,
    var calageEncoches:Boolean?,
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
    var status: Long?,
    var observations : String?,
    var poids : Long?,
    var tension: Long?
    ){
    fun toBobinage() : Bobinage {
        return Bobinage(
            _id,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            marqueMoteur,
            typeBobinage,
            puissance,
            vitesse,
            phases,
            frequences,
            courant,
            calageEncoches,
            null,
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