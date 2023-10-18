package com.calmwolfs.bedwar.events

import com.calmwolfs.bedwar.commands.CopyErrorCommand
import com.calmwolfs.bedwar.utils.RepoUtils
import com.google.gson.Gson
import java.io.File

class RepositoryReloadEvent(val repoLocation: File, val gson: Gson) : ModEvent() {
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