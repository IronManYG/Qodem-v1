package com.example.qodem.data.bloodbanks.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.qodem.data.Converters

@Database(entities = [BloodBankCacheEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class BloodBankDatabase: RoomDatabase() {

    abstract fun bloodBankDao(): BloodBankDao

    companion object{
        const val DATABASE_NAME: String = "bloodBank_db"
    }
}