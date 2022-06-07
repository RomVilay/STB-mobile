package com.example.applicationstb.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

import androidx.room.migration.Migration
import com.example.applicationstb.model.*


@Database(
    entities = arrayOf(
        ChantierEntity::class,
        BobinageEntity::class,
        DemontageTriphaseEntity::class,
        DemontageCCEntity::class,
        RemontageTriphaseEntity::class,
        RemontageCCEntity::class,
        DemontageMotopompeEntity::class,
        RemontageMotopompeEntity::class,
        DemontageMotoreducteurEntity::class,
        RemontageMotoreducteurEntity::class,
        DemontageReducteurEntity::class,
        DemoPompeEntity::class,
        DemontageAlternateurEntity::class,
        DemontageRotorBEntity::class,
        DemontageMonophaseEntity::class,
        ClientEntity::class,
        VehiculeEntity::class,
        RemontageEntity::class,
        PointageEntity::class,
        DemontageEntity::class
    ), version = 35
)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun chantierDao(): ChantierDao
    abstract fun bobinageDao(): BobinageDao
    abstract fun remontageDao(): RemontageDao
    abstract fun vehiculesDao(): VehiculeDao
    abstract fun clientDao(): ClientsDao
    abstract fun pointageDao(): PointageDao
    abstract fun demontageDao(): DemontageDao

    @Volatile
    private var INSTANCE: LocalDatabase? = null

    fun getDatabase(context: Context): LocalDatabase {
        // if the INSTANCE is not null, then return it,
        // if it is, then create the database
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                LocalDatabase::class.java,
                "database-local"
            ).build()
            INSTANCE = instance
            // return instance
            instance
        }
    }
}