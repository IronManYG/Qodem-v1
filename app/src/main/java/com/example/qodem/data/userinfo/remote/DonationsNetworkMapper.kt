package com.example.qodem.data.userinfo.remote

import com.example.qodem.model.Donation
import com.example.qodem.utils.EntityMapper
import javax.inject.Inject

class DonationsNetworkMapper
@Inject
constructor() : EntityMapper<DonationNetworkEntity, Donation> {
    override fun mapFromEntity(entity: DonationNetworkEntity): Donation {
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

    override fun mapToEntity(domainModel: Donation): DonationNetworkEntity {
        return DonationNetworkEntity(
            id = domainModel.id,
            bloodBankID = domainModel.bloodBankID,
            donationData = domainModel.donationData,
            donationTime = domainModel.donationTime,
            active = domainModel.active,
            authenticated = domainModel.authenticated,
            timeStamp = domainModel.donationDataTimeStamp
        )
    }

    fun mapFromEntityList(entities: List<DonationNetworkEntity>): List<Donation> {
        return entities.map { mapFromEntity(it) }
    }
}