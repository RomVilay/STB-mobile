package com.example.applicationstb.localdatabase

import androidx.room.*

@Dao
interface DemontageDao {
    @Insert
    fun insertAll(vararg fiches:DemontageEntity )

    @Delete
    fun delete(fiche: DemontageEntity )

    @Query("SELECT * FROM demontage")
    fun getAll(): List<DemontageEntity>

    @Query("SELECT * FROM demontage WHERE _id LIKE :id")
    fun getById(id: String) : DemontageEntity

    @Update
    fun update(vararg fiches:DemontageEntity)
}