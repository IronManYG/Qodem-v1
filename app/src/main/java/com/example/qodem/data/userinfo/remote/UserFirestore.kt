package com.example.qodem.data.userinfo.remote

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.qodem.utils.Result
import kotlinx.coroutines.tasks.await

class UserFirestore {

    private val usersCollectionRef = Firebase.firestore.collection("users")

    suspend fun getUserInfo(phoneNumber: String) = withContext(Dispatchers.IO) {
        Log.d("UserFirestore", "Start")
        try {
            lateinit var userInfo: UserNetworkEntity

            val userInfoQuery = usersCollectionRef
                .whereEqualTo("phoneNumber", phoneNumber)
                .get()
                .await()
            if (userInfoQuery.documents.isNotEmpty()) {
                for (document in userInfoQuery.documents) {
                    userInfo = document.toObject<UserNetworkEntity>()!!
                }
                return@withContext Result.Success(userInfo)
            } else {
                return@withContext Result.Error("User not found!")
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    suspend fun saveUserInfo(userNetworkEntity: UserNetworkEntity) = withContext(Dispatchers.IO) {
        usersCollectionRef.add(userNetworkEntity).await()
    }!!

}