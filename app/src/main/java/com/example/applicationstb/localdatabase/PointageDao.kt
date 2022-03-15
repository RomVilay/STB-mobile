package com.example.applicationstb.localdatabase

import androidx.room.*

@Dao
interface PointageDao {
    @Insert
    fun insertAll(vararg fiches:PointageEntity)

    @Delete
    fun delete(fiche: PointageEntity)

    @Query("SELECT * FROM pointages")
    fun getAll(): List<PointageEntity>

    @Query("SELECT * FROM pointages WHERE _id LIKE :id")
    fun getById(id: String) : PointageEntity

    @Update
    fun update(vararg fiches:PointageEntity)

}