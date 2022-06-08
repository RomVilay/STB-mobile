package com.example.applicationstb.localdatabase

import androidx.room.*
@Dao
interface RemontageDao {
    @Insert
    fun insertAll(vararg fiches:RemontageEntity)

    @Delete
    fun delete(fiche: RemontageEntity)

    @Query("SELECT * FROM remontage")
    fun getAll(): List<RemontageEntity>

    @Query("SELECT * FROM remontage WHERE _id LIKE :id")
    fun getById(id: String) : RemontageEntity

    @Update
    fun update(vararg fiches:RemontageEntity)
}