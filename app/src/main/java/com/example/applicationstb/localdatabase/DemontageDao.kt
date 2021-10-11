package com.example.applicationstb.localdatabase

import androidx.room.*
import com.example.applicationstb.model.DemontagePompe

@Dao
interface DemontageTriphaseDao {
    @Insert
    fun insertAll(vararg fiches:DemontageTriphaseEntity )

    @Delete
    fun delete(fiche: DemontageTriphaseEntity )

    @Query("SELECT * FROM demontage_triphase")
    fun getAll(): List<DemontageTriphaseEntity>

    @Query("SELECT * FROM demontage_triphase WHERE _id LIKE :id")
    fun getById(id: String) : DemontageTriphaseEntity

    @Update
    fun update(vararg fiches:DemontageTriphaseEntity)
}

@Dao
interface DemontageCCDao{
    @Insert
    fun insertAll(vararg fiches:DemontageCCEntity)

    @Delete
    fun delete(fiche: DemontageCCEntity)

    @Query("SELECT * FROM demontage_cc")
    fun getAll(): List<DemontageCCEntity>

    @Query("SELECT * FROM demontage_cc WHERE _id LIKE :id")
    fun getById(id: String) : DemontageCCEntity

    @Update
    fun update(vararg fiches:DemontageCCEntity)
}
@Dao
interface DemontagePDao{
    @Insert
    fun insertAll(vararg fiches:DemoPompeEntity)

    @Delete
    fun delete(fiche: DemoPompeEntity)

    @Query("SELECT * FROM demontage_pompe")
    fun getAll(): List<DemoPompeEntity>

    @Query("SELECT * FROM demontage_pompe WHERE _id LIKE :id")
    fun getById(id: String) : DemoPompeEntity

    @Update
    fun update(vararg fiches:DemoPompeEntity)
}