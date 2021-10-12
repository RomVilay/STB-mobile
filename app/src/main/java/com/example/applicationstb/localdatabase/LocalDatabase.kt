package com.example.applicationstb.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.applicationstb.model.DemontageAlternateur


@Database(entities = arrayOf(ChantierEntity::class, BobinageEntity::class, DemontageTriphaseEntity::class, DemontageCCEntity::class, RemontageTriphaseEntity::class, RemontageCCEntity::class, DemoPompeEntity::class, DemontageAlternateurEntity::class,DemontageRotorBEntity::class,DemontageMonophaseEntity::class), version = 14)
@TypeConverters (Converters::class)
    abstract class LocalDatabase : RoomDatabase() {
        abstract fun chantierDao(): ChantierDao
        abstract fun bobinageDao(): BobinageDao
        abstract fun demontageTriphaseDao(): DemontageTriphaseDao
        abstract fun demontageCCDao(): DemontageCCDao
        abstract fun demontagePDao(): DemontagePDao
        abstract fun demontageAlternateurDao(): DemontageAlternateurDao
        abstract fun demontageMonophaseDao(): DemontageMonophaseDao
        abstract fun demontageRotorBobineDao(): DemontageRotorBobineDao
        abstract fun remontageTriphaseDao(): RemontageTriphaseDao
        abstract fun remontageCCDao(): RemontageCCDao
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