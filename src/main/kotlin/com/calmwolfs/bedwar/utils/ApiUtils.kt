package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.commands.CopyErrorCommand
import com.calmwolfs.bedwar.utils.JsonUtils.getIntOr
import com.calmwolfs.bedwar.utils.JsonUtils.getStringOr
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import org.apache.http.util.EntityUtils
import kotlin.concurrent.fixedRateTimer

object ApiUtils {
    private val config get() = BedWarMod.feature.dev
    private val parser = JsonParser()

    private val nameToUuid = mutableMapOf<String, String>()
    private val currentFetchedNames = mutableListOf<String>()
    private val currentFetchedUuids = mutableListOf<String>()

    val uuidToName = mutableMapOf<String, String>()
    val uuidToExp = mutableMapOf<String, Int>()

    private const val maxRequestsPerMin = 55
    private const val maxRequestBank = 250
    var requestBank = 250

    init {
        fixedRateTimer(name = "bedwar-api-rate-limit", period = 60_000L) {
            try {
                requestBank += maxRequestsPerMin
                if (requestBank > maxRequestBank) requestBank = maxRequestBank
            } catch (error: Throwable) {
                CopyErrorCommand.logError(error, "Error updating api rate limit!")
            }
        }
    }

    private val builder: HttpClientBuilder =
        HttpClients.custom().setUserAgent("BedWar/${BedWarMod.version}")
            .setDefaultHeaders(
                mutableListOf(
                    BasicHeader("Pragma", "no-cache"),
                    BasicHeader("Cache-Control", "no-cache")
                )
            )
            .setDefaultRequestConfig(
                RequestConfig.custom()
                    .build()
            )
            .useSystemProperties()

    private fun getJSONResponse(urlString: String): JsonObject {
        val client = builder.build()

        try {
            val response = client.execute(HttpGet(urlString))
            val entity = response.entity
            val retSrc = EntityUtils.toString(entity)

            return try {
                parser.parse(retSrc) as JsonObject
            } catch (e: JsonSyntaxException) {
                CopyErrorCommand.logError(e, "Api JSON syntax error")
                JsonObject()
            }
        } catch (e: Exception) {
            CopyErrorCommand.logError(e, "Api error")
            JsonObject()
        } finally {
            client.close()
        }
        return JsonObject()
    }

    suspend fun getUuid(playerName: String): String {
        val fetchedName = playerName.lowercase()
        if (fetchedName in nameToUuid) return nameToUuid[fetchedName] ?: "-"

        if (fetchedName in currentFetchedNames) return "--"
        currentFetchedNames.add(fetchedName)

        val url = "https://api.mojang.com/users/profiles/minecraft/$fetchedName"

        var uuid = "-"
        try {
            val result = withContext(Dispatchers.IO) { getJSONResponse(url) }.asJsonObject
            uuid = result.getStringOr("id")
        } catch (_: Exception) {
            println("Mojang api issue")
        }

        nameToUuid[fetchedName] = uuid
        currentFetchedNames.remove(fetchedName)
        return uuid
    }

    suspend fun getBedwarsStats(uuid: String): JsonObject {
        val url = "https://api.hypixel.net/player?key=${config.apiKey}&uuid=$uuid"

        if (uuid in currentFetchedUuids) {
            return JsonObject()
        }
        currentFetchedUuids.add(uuid)
        requestBank--

        var stats = JsonObject()
        var displayName = "-"
        var experience = 0

        try {
            val result = withContext(Dispatchers.IO) { getJSONResponse(url) }.asJsonObject
            if (result.has("player")) {
                val playerJson = result.get("player").asJsonObject
                displayName = playerJson.getStringOr("displayname")
                if (playerJson.has("stats")) {
                    val statsJson = playerJson.get("stats").asJsonObject
                    if (statsJson.has("Bedwars")) {
                        stats = statsJson.get("Bedwars").asJsonObject
                        experience = stats.getIntOr("Experience")
                    }
                }
            }
        } catch (_: Exception) {
            println("Hypixel api issue")
        }

        uuidToName[uuid] = displayName
        uuidToExp[uuid] = experience
        currentFetchedUuids.remove(uuid)
        return stats
    }
}