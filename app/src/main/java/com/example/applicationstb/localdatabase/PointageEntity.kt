package com.example.applicationstb.localdatabase

import android.graphics.Point
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Pointage
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "pointages")
data class PointageEntity(
    @PrimaryKey var _id:String,
    var userId:String,
    var dateDebut: LocalDateTime?,
    var dateFin: LocalDateTime?
) {
    fun toPointage(): Pointage{
        return Pointage( _id,
            userId,
            dateDebut,
            dateFin)
    }
}
