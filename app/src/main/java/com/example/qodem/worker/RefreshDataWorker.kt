package com.example.qodem.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException

@HiltWorker
class RefreshDataWorker
@AssistedInject
constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val bloodBankRepository: BloodBankRepository,
) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            Log.d(WORK_NAME, "workStart,doWork")
            bloodBankRepository.getBloodBanks()
            Log.d(WORK_NAME, "getBloodBanks")
            Result.success()
        } catch (e: HttpException) {
            Log.d(WORK_NAME, "Error")
            Result.retry()
        }
    }
}
