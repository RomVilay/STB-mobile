package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Vehicule

@Entity(tableName = "vehicules")
data class VehiculeEntity (
    @PrimaryKey var _id:String,
    var nom:String?,
    var kilometrage:Int?) {
    fun toVehicule() :Vehicule{
        return Vehicule(_id,nom,kilometrage)
    }

}
