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

    @Query("SELECT * FROM Miasto LIMIT 1")
    suspend fun getFirstMiasto(): Miasto?


    @Query("SELECT COUNT(*) FROM Miasto")
    suspend fun getCityCount(): Int

    @Insert
    fun insertAll(vararg miasto: Miasto)


    @Query("DELETE FROM Miasto")
    suspend fun deleteAllCities()


    @Delete
    fun delete(miasto: Miasto)

}