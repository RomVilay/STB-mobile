package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Client

@Entity(tableName = "clients")
data class ClientEntity (
    @PrimaryKey var _id:String,
    var enterprise: String?,
    var address: String?,
    var telephone: String?,
    var contact: String?
        ) {
    fun toClient() : Client {
        return Client(_id,enterprise,telephone,address,contact)
    }
}