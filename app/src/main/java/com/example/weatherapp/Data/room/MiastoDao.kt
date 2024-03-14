package com.example.weatherapp.Data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MiastoDao {
    @Query("SELECT * FROM miasto")
    fun getAll(): List<Miasto>

    @Insert
    fun insertAll(vararg miasto: Miasto)

    @Delete
    fun delete(miasto: Miasto)
}