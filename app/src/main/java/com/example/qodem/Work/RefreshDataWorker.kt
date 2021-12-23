package com.example.qodem.Work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.SavedStateHandle
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.qodem.data.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.HttpException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltWorker
class RefreshDataWorker
@Inject
constructor(appContext: Context,
            params: WorkerParameters,
            private val mainRepository: MainRepository,
            private val savedStateHandle: SavedStateHandle
): CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        Log.d("here", "workStart,doWork")
        return try {
            mainRepository.getBloodBanks()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}
