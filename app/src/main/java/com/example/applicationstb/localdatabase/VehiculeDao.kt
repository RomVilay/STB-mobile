package com.example.applicationstb.localdatabase

import androidx.room.*

@Dao
interface VehiculeDao {
    @Insert
    fun insertAll(vararg vehicules:VehiculeEntity)

    @Delete
    fun delete(vehicule: VehiculeEntity)

    @Query("SELECT * FROM vehicules")
    fun getAll(): List<VehiculeEntity>

    @Query("SELECT * FROM vehicules WHERE _id LIKE :id")
    fun getById(id: String) : VehiculeEntity

    @Update
    fun update(vararg vehicules:VehiculeEntity)

}