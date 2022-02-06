package com.example.qodem.data.userinfo.local

import com.example.qodem.model.Donation

import com.example.qodem.utils.EntityMapper
import javax.inject.Inject

class DonationsCacheMapper
@Inject
constructor() : EntityMapper<DonationsCacheEntity, Donation> {
    override fun mapFromEntity(entity: DonationsCacheEntity): Donation {
        return Donation(
            id = entity.id,
            bloodBankID = entity.bloodBankID,
            donationData = entity.donationData,
            donationTime = entity.donationTime,
            active = entity.active,
            authenticated = entity.authenticated,
            donationDataTimeStamp = entity.timeStamp
        )
    }

    override fun mapToEntity(domainModel: Donation): DonationsCacheEntity {
        return DonationsCacheEntity(
            id = domainModel.id,
            bloodBankID = domainModel.bloodBankID,
            donationData = domainModel.donationData,
            donationTime = domainModel.donationTime,
            active = domainModel.active,
            authenticated = domainModel.authenticated,
            timeStamp = domainModel.donationDataTimeStamp
        )
    }

    fun mapFromEntityList(entities: List<DonationsCacheEntity>): List<Donation>{
        return entities.map {mapFromEntity(it)}
    }

    fun mapToEntityList(entities: List<Donation>): List<DonationsCacheEntity>{
        return entities.map {mapToEntity(it)}
    }
}