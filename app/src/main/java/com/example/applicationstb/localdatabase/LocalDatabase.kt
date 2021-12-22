package com.example.applicationstb.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.applicationstb.model.DemontageAlternateur
import androidx.sqlite.db.SupportSQLiteDatabase

import androidx.room.migration.Migration





@Database(entities = arrayOf(ChantierEntity::class, BobinageEntity::class, DemontageTriphaseEntity::class, DemontageCCEntity::class, RemontageTriphaseEntity::class, RemontageCCEntity::class, DemoPompeEntity::class, DemontageAlternateurEntity::class,DemontageRotorBEntity::class,DemontageMonophaseEntity::class,ClientEntity::class,VehiculeEntity::class, RemontageEntity::class), version = 20)
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
        abstract fun remontageDao(): RemontageDao
        abstract fun vehiculesDao(): VehiculeDao
        abstract  fun clientDao(): ClientsDao
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
    val MIGRATION_20_21  = object : Migration(20, 21) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create the new table
            database.execSQL(
                "CREATE TABLE demontage_pompe_new (_id TEXT NOT NULL,numDevis TEXT, numFiche TEXT, statut INTEGER,client TEXT NOT NULL, contact TEXT, telContact TEXT, dateDebut INTEGER, dureeTotale INTEGER, observation TEXT, photos TEXT, typeFicheDemontage INTEGER NOT NULL, typeMoteur TEXT, marque TEXT, numSerie TEXT, fluide TEXT, sensRotation INTEGER, typeRessort INTEGER, typeJoint TEXT, matiere INTEGER, diametreArbre TEXT, diametreExtPR TEXT, diametreExtPF TEXT, epaisseurPF TEXT, longueurRotativeNonComprimee TEXT, longueurRotativeComprimee TEXT, longueurRotativeTravail TEXT, PRIMARY KEY (_id))"
            )
            // Copy the data
            database.execSQL(
                "INSERT INTO demontage_pompe_new (_id, numDevis, numFiche,statut, client, contact, telContact,dateDebut, dureeTotale, observation, photos, typeFicheDemontage, typeMoteur, marque, numSerie,fluide, sensRotation, typeRessort, typeJoint, matiere, diametreArbre, diametreExtPR, diametreExtPF, epaisseurPF, longueurRotativeNonComprimee, longueurRotativeComprimee, longueurRotativeTravail) SELECT _id, numDevis, numFiche,statut, client, contact, telContact,dateDebut, dureeTotale, observation, photos, typeFicheDemontage, typeMoteur, marque, numSerie,fluide, sensRotation, typeRessort, typeJoint, matiere, diametreArbre, diametreExtPR, diametreExtPF, epaisseurPF, longueurRotativeNonComprimee, longueurRotativeComprimee, longueurRotativeTravail FROM demontage_pompe"
            )
            // Remove the old table
            database.execSQL("DROP TABLE demontage_pompe")
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE demontage_pompe_new RENAME TO demontage_pompe")
        }
    }
    }