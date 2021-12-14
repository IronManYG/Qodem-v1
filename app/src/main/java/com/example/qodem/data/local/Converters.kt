package com.example.qodem.data.local

import androidx.room.TypeConverter
import com.example.qodem.model.Coordinates
import com.example.qodem.model.WorkingDays
import com.example.qodem.model.WorkingHours
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromWorkingHours(workingHours: WorkingHours): String? {
        return Gson().toJson(workingHours)
    }

    @TypeConverter
    fun toWorkingHours(workingHours: String): WorkingHours? {
        return Gson().fromJson(workingHours,WorkingHours::class.java)
    }

    @TypeConverter
    fun fromWorkingDays(workingDays: WorkingDays): String? {
        return Gson().toJson(workingDays)
    }

    @TypeConverter
    fun toWorkingDays(workingDays: String): WorkingDays? {
        return Gson().fromJson(workingDays,WorkingDays::class.java)
    }

    @TypeConverter
    fun fromCoordinates(coordinates: Coordinates): String? {
        return Gson().toJson(coordinates)
    }

    @TypeConverter
    fun toCoordinates(coordinates: String): Coordinates? {
        return Gson().fromJson(coordinates,Coordinates::class.java)
    }
}