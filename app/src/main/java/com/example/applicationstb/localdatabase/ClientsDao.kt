package com.example.applicationstb.localdatabase

import androidx.room.*

@Dao
interface ClientsDao {
    @Insert
    fun insertAll(vararg clients:ClientEntity)

    @Delete
    fun delete(client: ClientEntity)

    @Query("SELECT * FROM clients")
    fun getAll(): List<ClientEntity>

    @Query("SELECT * FROM clients WHERE _id LIKE :id")
    fun getById(id: String) : ClientEntity

    @Update
    fun update(vararg clients:ClientEntity)

}