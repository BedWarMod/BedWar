package com.calmwolfs.bedwar.events

import com.calmwolfs.bedwar.commands.CopyErrorCommand
import com.calmwolfs.bedwar.utils.RepoUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.File

class RepositoryReloadEvent(val repoLocation: File, val gson: Gson) : ModEvent() {
    fun getConstant(constant: String) = getConstant<JsonObject>(constant)

    inline fun <reified T : Any> getConstant(constant: String) = try {
        RepoUtils.getConstant(repoLocation, constant, gson, T::class.java)
    } catch (e: Exception) {
        CopyErrorCommand.logError(
            Exception("Repo parsing error while trying to read constant '$constant'", e),
            "Error reading repo data"
        )
        null
    }
}