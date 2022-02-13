package com.example.qodem.data.userinfo.remote

import com.example.qodem.utils.Result
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserFirestore {
    private val usersCollectionRef = Firebase.firestore.collection("users")

    private lateinit var userFirestoreID: String

    private lateinit var donationsCollectionRef: CollectionReference

    suspend fun getUserInfo(phoneNumber: String) = withContext(Dispatchers.IO) {
        try {
            lateinit var userInfo: UserNetworkEntity
            val userInfoQuery = usersCollectionRef
                .whereEqualTo("phoneNumber", phoneNumber)
                .get()
                .await()
            if (userInfoQuery.documents.isNotEmpty()) {
                for (document in userInfoQuery.documents) {
                    userInfo = document.toObject<UserNetworkEntity>()!!
                    userInfo.id = document.id
                    userFirestoreID = userInfo.id
                    donationsCollectionRef = usersCollectionRef.document(userFirestoreID).collection("donations")
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
        try {
            usersCollectionRef.add(userNetworkEntity).await()
            return@withContext Result.Success("User Successful Saved")
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    suspend fun getAllDonations() = withContext(Dispatchers.IO) {
        try {
            lateinit var userDonation: DonationNetworkEntity
            val userDonations: MutableList<DonationNetworkEntity> = mutableListOf()
            val donationsQuerySnapshot = donationsCollectionRef.get().await()
            if (donationsQuerySnapshot.documents.isNotEmpty()) {
                for (document in donationsQuerySnapshot.documents) {
                    userDonation = document.toObject<DonationNetworkEntity>()!!
                    userDonation.id = document.id
                    userDonations.add(userDonation)
                }
                return@withContext Result.Success(userDonations)
            } else {
                return@withContext Result.Error("There is no donations")
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }

    }

    suspend fun saveDonation(donationNetworkEntity: DonationNetworkEntity) =
        withContext(Dispatchers.IO) {
            try {
                donationsCollectionRef.add(donationNetworkEntity).await()
                return@withContext Result.Success("Donation Successful Saved")
            } catch (e: Exception) {
                return@withContext Result.Error(e.localizedMessage)
            }
        }

    suspend fun updateDonationActiveState(donationID: String, isActive: Boolean) = withContext(Dispatchers.IO) {
        val donationDocumentRef = donationsCollectionRef.document(donationID)
        try {
            donationDocumentRef.update("active",isActive).await()
            return@withContext Result.Success("Donation active state Successful Update")
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    suspend fun updateDonationAuthenticatedState(donationID: String, isActive: Boolean) = withContext(Dispatchers.IO) {
        val donationDocumentRef = donationsCollectionRef.document(donationID)
        try {
            Firebase.firestore.runBatch { batch ->
                batch.update(donationDocumentRef,"authenticated",isActive)
                batch.update(donationDocumentRef,"active",!isActive)
            }.await()
            //donationDocumentRef.update("authenticated",isActive).await()
            return@withContext Result.Success("Donation Authenticated state Successful Update")
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    suspend fun updateUserName(userID: String, firstName: String, lastName: String) = withContext(Dispatchers.IO){
        val userDocumentRef = usersCollectionRef.document(userID)
        try {
            Firebase.firestore.runBatch { batch ->
                batch.update(userDocumentRef,"firstName",firstName)
                batch.update(userDocumentRef,"lastName",lastName)
            }.await()
            return@withContext Result.Success("User name Successful Update")
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    suspend fun updateUserDateOFBirth(userID: String, dateOfBirth: String) = withContext(Dispatchers.IO){
        val userDocumentRef = usersCollectionRef.document(userID)
        try {
            userDocumentRef.update("birthDate",dateOfBirth).await()
            return@withContext Result.Success("User birth Date Successful Update")
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    suspend fun updateUserBloodType(userID: String, bloodType: String) = withContext(Dispatchers.IO){
        val userDocumentRef = usersCollectionRef.document(userID)
        try {
            userDocumentRef.update("bloodType",bloodType).await()
            return@withContext Result.Success("User blood type Successful Update")
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    suspend fun updateUserGender(userID: String, gender: String) = withContext(Dispatchers.IO){
        val userDocumentRef = usersCollectionRef.document(userID)
        try {
            userDocumentRef.update("gender",gender).await()
            return@withContext Result.Success("User gender Successful Update")
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    suspend fun updateUserCity(userID: String, city: String) = withContext(Dispatchers.IO){
        val userDocumentRef = usersCollectionRef.document(userID)
        try {
            userDocumentRef.update("city",city).await()
            return@withContext Result.Success("User city Successful Update")
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    suspend fun updateUserID(userID: String, idType: String, idNumber: String) = withContext(Dispatchers.IO){
        val userDocumentRef = usersCollectionRef.document(userID)
        try {
            Firebase.firestore.runBatch { batch ->
                batch.update(userDocumentRef,"idtype",idType)
                batch.update(userDocumentRef,"idnumber",idNumber)
            }.await()
            return@withContext Result.Success("User id type & id number Successful Update")
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

}