package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.applicationstb.model.Chantier
import java.util.*

@Entity (tableName="chantiers")
data class ChantierEntity(
    @PrimaryKey var _id:String,
    var status: Long,
    var client: String,
    var contact: String?,
    var telContact: String?,
    var dateDebut: Date?,
    var dureeTotale: String?,
    var observations: String?,
   // var photo: Array<String>?,
    var vehicule: String?,
    var adresseChantier:String?,
    var objet:String?,
    var materiel:String?,
    var diagnostic:String?,
    var signatureTech:String?,
    var signatureClient:String?
){
    fun toChantier() : Chantier{
        var ch = Chantier(_id,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
        ch.materiel = materiel
        ch.objet =  objet
        ch.observations = observations
        ch.status = status
        return ch
    }
}

class Converters {
    @TypeConverter
    fun fromDate(value:Long?) : Date? {
        return value?.let { Date(it) }
    }
    @TypeConverter
    fun dateToTimestamp(date: Date?) : Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun arrayToJSON(value: Array<String>?) : String? {
        return value!!.joinToString( separator = ",")
    }
    @TypeConverter
    fun arrayFromJSON(value:String) : Array<String> {
        var tab: Array<String> = value.split(",").map { it }.toTypedArray()
        return tab
    }

}
