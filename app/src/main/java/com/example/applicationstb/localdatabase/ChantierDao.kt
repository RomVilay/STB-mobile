package com.example.applicationstb.localdatabase

import androidx.room.*

@Dao
interface ChantierDao {
    @Insert
    fun insertAll(vararg fiches:ChantierEntity)

    @Delete
    fun delete(fiche: ChantierEntity)

    @Query("SELECT * FROM chantiers")
    fun getAll(): List<ChantierEntity>

    @Query("SELECT * FROM chantiers WHERE _id LIKE :id")
    fun getById(id: String) : ChantierEntity

    @Update
    fun update(vararg fiches:ChantierEntity)

}