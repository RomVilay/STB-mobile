package com.example.applicationstb.localdatabase

import androidx.room.*
import com.example.applicationstb.model.DemontageMonophase
import com.example.applicationstb.model.DemontagePompe
import com.example.applicationstb.model.DemontageRotorBobine

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
@Dao
interface DemontageAlternateurDao{
    @Insert
    fun insertAll(vararg fiches:DemontageAlternateurEntity)

    @Delete
    fun delete(fiche: DemontageAlternateurEntity)

    @Query("SELECT * FROM demontage_alternateur")
    fun getAll(): List<DemontageAlternateurEntity>

    @Query("SELECT * FROM demontage_alternateur WHERE _id LIKE :id")
    fun getById(id: String) : DemontageAlternateurEntity

    @Update
    fun update(vararg fiches:DemontageAlternateurEntity)
}
@Dao
interface DemontageMonophaseDao{
    @Insert
    fun insertAll(vararg fiches:DemontageMonophaseEntity)

    @Delete
    fun delete(fiche: DemontageMonophaseEntity)

    @Query("SELECT * FROM demontage_monophase")
    fun getAll(): List<DemontageMonophaseEntity>

    @Query("SELECT * FROM demontage_monophase WHERE _id LIKE :id")
    fun getById(id: String) : DemontageMonophaseEntity

    @Update
    fun update(vararg fiches:DemontageMonophaseEntity)
}
@Dao
interface DemontageRotorBobineDao{
    @Insert
    fun insertAll(vararg fiches:DemontageRotorBEntity)

    @Delete
    fun delete(fiche: DemontageRotorBEntity)

    @Query("SELECT * FROM demontage_rotorb")
    fun getAll(): List<DemontageRotorBEntity>

    @Query("SELECT * FROM demontage_rotorb WHERE _id LIKE :id")
    fun getById(id: String) : DemontageRotorBEntity

    @Update
    fun update(vararg fiches:DemontageRotorBEntity)
}
@Dao
interface DemontageMotopompeDao{
    @Insert
    fun insertAll(vararg fiches:DemontageMotopompeEntity)

    @Delete
    fun delete(fiche: DemontageMotopompeEntity)

    @Query("SELECT * FROM demontage_motopompe")
    fun getAll(): List<DemontageMotopompeEntity>

    @Query("SELECT * FROM demontage_motopompe WHERE _id LIKE :id")
    fun getById(id: String) : DemontageMotopompeEntity

    @Update
    fun update(vararg fiches:DemontageMotopompeEntity)
}
@Dao
interface DemontageMotoreducteurDao{
    @Insert
    fun insertAll(vararg fiches:DemontageMotoreducteurEntity)

    @Delete
    fun delete(fiche: DemontageMotoreducteurEntity)

    @Query("SELECT * FROM demontage_motoreducteur")
    fun getAll(): List<DemontageMotoreducteurEntity>

    @Query("SELECT * FROM demontage_motoreducteur WHERE _id LIKE :id")
    fun getById(id: String) : DemontageMotoreducteurEntity

    @Update
    fun update(vararg fiches:DemontageMotoreducteurEntity)
}
interface DemontageReducteurDao{
    @Insert
    fun insertAll(vararg fiches:DemontageReducteurEntity)

    @Delete
    fun delete(fiche: DemontageReducteurEntity)

    @Query("SELECT * FROM demontage_reducteur")
    fun getAll(): List<DemontageReducteurEntity>

    @Query("SELECT * FROM demontage_reducteur WHERE _id LIKE :id")
    fun getById(id: String) : DemontageReducteurEntity

    @Update
    fun update(vararg fiches:DemontageReducteurEntity)
}