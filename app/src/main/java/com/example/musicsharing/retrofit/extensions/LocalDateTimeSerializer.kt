package com.example.musicsharing.retrofit.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.format.DateTimeFormatter

class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime> {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
        val formatter = DateTimeFormatter.ISO_DATE_TIME // Assuming the format is ISO_DATE_TIME
        return LocalDateTime.parse(json?.asString, formatter)
    }
}