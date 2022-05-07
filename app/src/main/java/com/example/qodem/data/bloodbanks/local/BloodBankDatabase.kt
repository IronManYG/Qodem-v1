package com.example.qodem.data.bloodbanks.local

import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.qodem.data.Converters
import com.example.qodem.data.bloodbanks.remote.BloodBankNetworkMapper
import com.example.qodem.data.bloodbanks.remote.BloodBanksRetrofit
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
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
        private const val TAG = "BloodBankDatabase"
    }

    abstract fun bloodBankDao(): BloodBankDao

    class Callback
    @Inject
    constructor(
        private val database: Provider<BloodBankDatabase>,
        private val bloodBanksRetrofit: BloodBanksRetrofit,
        private val bloodBankCacheMapper: BloodBankCacheMapper,
        private val bloodBankNetworkMapper: BloodBankNetworkMapper,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val bloodBankDao = database.get().bloodBankDao()

            applicationScope.launch {
                val networkBloodBanksResponse = bloodBanksRetrofit.getBloodBanks()
                if (networkBloodBanksResponse.isSuccessful && networkBloodBanksResponse.body() != null) {
                    val bloodBanks =
                        bloodBankNetworkMapper.mapFromEntityList(networkBloodBanksResponse.body()!!)
                    for (bloodBank in bloodBanks) {
                        bloodBankDao.saveBloodBank(bloodBankCacheMapper.mapToEntity(bloodBank))
                    }
                    Log.d(TAG, "Blood Banks found!")
                } else {
                    val message = networkBloodBanksResponse.message()
                    Log.e(TAG, message)
                    Log.e(TAG, "Response not successful")
                }
            }
        }
    }
}