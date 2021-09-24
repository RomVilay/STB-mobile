package com.example.applicationstb.localdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChantierDao {
    @Insert
    fun insertAll(vararg fiches:ChantierEntity)

    @Delete
    fun delete(fiche: ChantierEntity)

    @Query("SELECT * FROM chantiers")
    fun getAll(): List<ChantierEntity>

}