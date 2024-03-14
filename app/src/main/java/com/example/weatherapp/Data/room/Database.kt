package com.example.weatherapp.Data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Miasto::class], version = 1)
abstract class Database : RoomDatabase(){
    abstract fun userDao(): MiastoDao
}