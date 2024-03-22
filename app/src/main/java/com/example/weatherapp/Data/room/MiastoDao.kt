package com.example.weatherapp.Data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MiastoDao {
    @Query("SELECT * FROM miasto")
    fun getAll(): Flow<List<Miasto>> // Return Flow<List<Miasto>>

    @Insert
    fun insertAll(vararg miasto: Miasto)

    @Delete
    fun delete(miasto: Miasto)

}