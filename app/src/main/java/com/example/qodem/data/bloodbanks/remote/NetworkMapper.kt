package com.example.qodem.data.bloodbanks.remote

import com.example.qodem.model.BloodBank
import com.example.qodem.model.Coordinates
import com.example.qodem.utils.EntityMapper
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class NetworkMapper
@Inject
constructor():
    EntityMapper<BloodBankNetworkEntity, BloodBank> {

    override fun mapFromEntity(entity: BloodBankNetworkEntity): BloodBank {
        return BloodBank(
            id = entity.id,
            name_en = entity.name_en,
            name_ar = entity.name_ar,
            city = entity.city,
            workingHours = entity.workingHours,
            workingDays = entity.workingDays,
            classification = entity.classification,
            phoneNumber = entity.phoneNumber,
            coordinates = LatLng(entity.coordinates.lat,entity.coordinates.lng),
            gapBetweenAppointment = entity.gapBetweenAppointment,
            donorLimit = entity.donorLimit,
            bloodDonationCampaign = entity.bloodDonationCampaign,
            campaignPeriod = entity.campaignPeriod
        )
    }

    override fun mapToEntity(domainModel: BloodBank): BloodBankNetworkEntity {
        return BloodBankNetworkEntity(
            id = domainModel.id,
            name_en = domainModel.name_en,
            name_ar = domainModel.name_ar,
            city = domainModel.city,
            workingHours = domainModel.workingHours,
            workingDays = domainModel.workingDays,
            classification = domainModel.classification,
            phoneNumber = domainModel.phoneNumber,
            coordinates = Coordinates(domainModel.coordinates.latitude,domainModel.coordinates.longitude),
            gapBetweenAppointment = domainModel.gapBetweenAppointment,
            donorLimit = domainModel.donorLimit,
            bloodDonationCampaign = domainModel.bloodDonationCampaign,
            campaignPeriod = domainModel.campaignPeriod
        )
    }

    fun mapFromEntityList(entities: List<BloodBankNetworkEntity>): List<BloodBank>{
        return entities.map {mapFromEntity(it)}
    }

}