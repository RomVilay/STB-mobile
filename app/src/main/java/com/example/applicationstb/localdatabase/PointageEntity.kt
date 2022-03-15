package com.example.applicationstb.localdatabase

import android.graphics.Point
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Pointage
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

@Entity(tableName = "pointages")
data class PointageEntity(
    @PrimaryKey var _id:String,
    var user:String,
    var timestamp: ZonedDateTime
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toPointage(): Pointage{
        return Pointage( _id,
            user,
            timestamp)
    }
}
