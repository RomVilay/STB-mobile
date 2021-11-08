package com.example.applicationstb.model

import android.os.Parcelable
import com.example.applicationstb.localdatabase.ClientEntity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class Client(val _id:String, var enterprise: String?, var telephone: String?, var address: String?, var contact: String? ) : Parcelable {
    override fun toString(): String {
        return "{\"id\":\"${_id}\",\"enterprise\":\"${enterprise}\",\"telephone\":${telephone},\"address\":${address}}"
    }
    fun toEntity(): ClientEntity{
        return ClientEntity(_id,enterprise,telephone,address,contact)
    }
}