package com.example.qodem.data.bloodbanks.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.qodem.data.Converters
import com.example.qodem.di.module.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [BloodBankCacheEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class BloodBankDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME: String = "bloodBank_db"
    }

    abstract fun bloodBankDao(): BloodBankDao

    class Callback
    @Inject
    constructor(
        private val database: Provider<BloodBankDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().bloodBankDao()

            applicationScope.launch {
                // TODO: add blood banks to data base
            }
        }
    }
}