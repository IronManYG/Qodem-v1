package com.example.qodem.data.userinfo.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.qodem.data.Converters

@Database(entities = [UserCacheEntity::class, DonationsCacheEntity::class], version = 6)
@TypeConverters(Converters::class)
abstract class UserDatabase: RoomDatabase()  {

    abstract fun userDao() : UserDao

    companion object{
        const  val DATABASE_NAME: String = "user_db"
    }

}