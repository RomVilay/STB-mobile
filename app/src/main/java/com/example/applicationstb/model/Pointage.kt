package com.example.applicationstb.model

import android.os.Parcelable
import com.example.applicationstb.localdatabase.PointageEntity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*


class Pointage(
    var _id: String,
    var user: String,
    var timestamp: ZonedDateTime
) {
    fun toEntity(): PointageEntity {
        return PointageEntity(
            _id,
            user,
            timestamp
        )
    }
}