package com.calmwolfs.bedwar.utils

import com.google.gson.JsonObject

object JsonUtils {
    fun JsonObject.getIntOr(key: String, or: Int = 0): Int {
        return if (has(key)) get(key).asInt else or
    }

    fun JsonObject.getStringOr(key: String, or: String = "-"): String {
        return if (has(key)) get(key).asString else or
    }
}