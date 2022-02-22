package com.example.applicationstb.localdatabase

import androidx.room.*
import com.example.applicationstb.model.RemontageMotopompe
import com.example.applicationstb.model.RemontageMotoreducteur

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
@Dao
interface RemontageMotopompeDao {
    @Insert
    fun insertAll(vararg fiches:RemontageMotopompeEntity)

    @Delete
    fun delete(fiche: RemontageMotopompeEntity)

    @Query("SELECT * FROM remontage_motopompe")
    fun getAll(): List<RemontageMotopompeEntity>

    @Query("SELECT * FROM remontage_motopompe WHERE _id LIKE :id")
    fun getById(id: String) : RemontageMotopompeEntity

    @Update
    fun update(vararg fiches:RemontageMotopompeEntity)
}

@Dao
interface RemontageMotoreducteurDao {
    @Insert
    fun insertAll(vararg fiches:RemontageMotoreducteurEntity)

    @Delete
    fun delete(fiche: RemontageMotoreducteurEntity)

    @Query("SELECT * FROM remontage_motoreducteur")
    fun getAll(): List<RemontageMotoreducteurEntity>

    @Query("SELECT * FROM remontage_motoreducteur WHERE _id LIKE :id")
    fun getById(id: String) : RemontageMotoreducteurEntity

    @Update
    fun update(vararg fiches:RemontageMotoreducteurEntity)
}