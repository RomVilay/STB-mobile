package com.example.applicationstb.localdatabase

import androidx.room.*

@Dao
interface BobinageDao {
    @Insert
    fun insertAll(vararg fiches:BobinageEntity)

    @Delete
    fun delete(fiche: BobinageEntity)

    @Query("SELECT * FROM bobinages")
    fun getAll(): List<BobinageEntity>

    @Query("SELECT * FROM bobinages WHERE _id LIKE :id")
    fun getById(id: String) : BobinageEntity

    @Update
    fun update(vararg fiches:BobinageEntity)

}