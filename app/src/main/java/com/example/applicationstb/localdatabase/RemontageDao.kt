package com.example.applicationstb.localdatabase

import androidx.room.*

@Dao
interface RemontageTriphaseDao {
    @Insert
    fun insertAll(vararg fiches:RemontageTriphaseEntity)

    @Delete
    fun delete(fiche: RemontageTriphaseEntity)

    @Query("SELECT * FROM remontage_triphase")
    fun getAll(): List<RemontageTriphaseEntity>

    @Query("SELECT * FROM remontage_triphase WHERE _id LIKE :id")
    fun getById(id: String) : RemontageTriphaseEntity

    @Update
    fun update(vararg fiches:RemontageTriphaseEntity)
}

@Dao
interface RemontageCCDao {
    @Insert
    fun insertAll(vararg fiches:RemontageCCEntity)

    @Delete
    fun delete(fiche: RemontageCCEntity)

    @Query("SELECT * FROM remontage_cc")
    fun getAll(): List<RemontageCCEntity>

    @Query("SELECT * FROM remontage_cc WHERE _id LIKE :id")
    fun getById(id: String) : RemontageCCEntity

    @Update
    fun update(vararg fiches:RemontageCCEntity)
}
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