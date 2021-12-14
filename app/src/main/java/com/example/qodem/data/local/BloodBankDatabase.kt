package com.example.qodem.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [BloodBankCacheEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class BloodBankDatabase: RoomDatabase() {

    abstract fun bloodBankDao(): BloodBankDao

    companion object{
        val DATABASE_NAME: String = "bloodBank_db"
    }
}