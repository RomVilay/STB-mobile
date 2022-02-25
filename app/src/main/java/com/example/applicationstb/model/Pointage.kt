package com.example.applicationstb.model

import android.os.Parcelable
import com.example.applicationstb.localdatabase.PointageEntity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.*


class Pointage(
    var _id: String,
    var userId: String,
    var dateDebut: LocalDateTime?,
    var dateFin: LocalDateTime?
) {
    fun toEntity(): PointageEntity {
        return PointageEntity(
            _id,
            userId,
            dateDebut,
            dateFin
        )
    }
}