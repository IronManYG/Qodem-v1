package com.example.qodem.data.userinfo.remote

import com.example.qodem.model.User
import com.example.qodem.utils.EntityMapper
import javax.inject.Inject

class UserNetworkMapper @Inject
constructor() : EntityMapper<UserNetworkEntity, User> {
    override fun mapFromEntity(entity: UserNetworkEntity): User {
        return User(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            imageName = entity.imageName,
            bloodType = entity.bloodType,
            birthDate = entity.birthDate,
            city = entity.city,
            phoneNumber = entity.phoneNumber,
            numberOfDonations = entity.numberOfDonations,
            IDType = entity.IDType,
            IDNumber = entity.IDNumber,
            communityId = entity.communityId,
            donations = entity.donations
        )
    }

    override fun mapToEntity(domainModel: User): UserNetworkEntity {
        return UserNetworkEntity(
            id = domainModel.id,
            firstName = domainModel.firstName,
            lastName = domainModel.lastName,
            imageName = domainModel.imageName,
            bloodType = domainModel.bloodType,
            birthDate = domainModel.birthDate,
            city = domainModel.city,
            phoneNumber = domainModel.phoneNumber,
            numberOfDonations = domainModel.numberOfDonations,
            IDType = domainModel.IDType,
            IDNumber = domainModel.IDNumber,
            communityId = domainModel.communityId,
            donations = domainModel.donations
        )
    }

    fun mapFromEntityList(entities: List<UserNetworkEntity>): List<User>{
        return entities.map {mapFromEntity(it)}
    }
}