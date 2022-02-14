package com.example.qodem.data.userinfo.local

import com.example.qodem.model.User
import com.example.qodem.utils.EntityMapper
import javax.inject.Inject

class UserCacheMapper
@Inject
constructor() : EntityMapper<UserCacheEntity, User> {
    override fun mapFromEntity(entity: UserCacheEntity): User {
        return User(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            imageName = entity.imageName,
            bloodType = entity.bloodType,
            birthDate = entity.birthDate,
            gender = entity.gender,
            city = entity.city,
            phoneNumber = entity.phoneNumber,
            numberOfDonations = entity.numberOfDonations,
            IDType = entity.IDType,
            IDNumber = entity.IDNumber,
            communityId = entity.communityId,
            donations = entity.donations
        )
    }

    override fun mapToEntity(domainModel: User): UserCacheEntity {
        return UserCacheEntity(
            id = domainModel.id,
            firstName = domainModel.firstName,
            lastName = domainModel.lastName,
            imageName = domainModel.imageName,
            bloodType = domainModel.bloodType,
            birthDate = domainModel.birthDate,
            gender = domainModel.gender,
            city = domainModel.city,
            phoneNumber = domainModel.phoneNumber,
            numberOfDonations = domainModel.numberOfDonations,
            IDType = domainModel.IDType,
            IDNumber = domainModel.IDNumber,
            communityId = domainModel.communityId,
            donations = domainModel.donations
        )
    }

    fun mapFromEntityList(entities: List<UserCacheEntity>): List<User>{
        return entities.map {mapFromEntity(it)}
    }

    fun mapToEntityList(entities: List<User>): List<UserCacheEntity>{
        return entities.map {mapToEntity(it)}
    }

}