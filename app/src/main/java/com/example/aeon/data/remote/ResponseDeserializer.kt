package com.example.aeon.data.remote

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ResponseDeserializer<T> : JsonDeserializer<T> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T {
        val response = json.asJsonObject.get("response")
        return Gson().fromJson(response, typeOfT)
    }
}