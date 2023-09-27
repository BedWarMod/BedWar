package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.data.eums.BedwarsGameMode
import com.calmwolfs.bedwar.data.types.BedwarsPlayerData
import com.calmwolfs.bedwar.data.types.GameModeData
import com.calmwolfs.bedwar.utils.JsonUtils.getIntOr
import com.calmwolfs.bedwar.utils.computer.SimpleTimeMark
import com.google.gson.JsonObject
import kotlin.time.Duration.Companion.seconds

object StatUtils {
    private val playerDataCache = mutableMapOf<String, BedwarsPlayerData>()

    suspend fun getPlayerData(uuid: String): BedwarsPlayerData {
        if (playerDataCache.containsKey(uuid)) {
            var playerData = playerDataCache[uuid] ?: BedwarsPlayerData()

            if (playerData.lastUpdated.passedSince() > 300.seconds && ApiUtils.requestBank > 0) {
                val playerStats = ApiUtils.getBedwarsStats(uuid)
                if (playerStats.entrySet().isEmpty()) {
                    ChatUtils.chat("§6[BW] §eHypixel Api Issue, maybe rate limited!")
                }
                playerData = getBedwarsPlayerData(uuid, playerStats)
                playerDataCache[uuid] = playerData
            }

            return playerData
        }

        if (ApiUtils.requestBank < 1) return BedwarsPlayerData()

        val playerStats = ApiUtils.getBedwarsStats(uuid)
        if (playerStats.entrySet().isEmpty()) {
            ChatUtils.chat("§6[BW] §eHypixel Api Issue, maybe rate limited!")
        }
        val playerData = getBedwarsPlayerData(uuid, playerStats)
        playerDataCache[uuid] = playerData
        return playerData
    }

    private fun getBedwarsPlayerData(uuid: String, stats: JsonObject): BedwarsPlayerData {
        val playerName = ApiUtils.uuidToName[uuid] ?: "-"
        val experience = ApiUtils.uuidToExp[uuid] ?: -1
        val timeStamp = if (stats.entrySet().isEmpty()) SimpleTimeMark.farPast() else SimpleTimeMark.now()

        val overallStats = mapGameModeStats(BedwarsGameMode.OVERALL, stats)
        val foursStats = mapGameModeStats(BedwarsGameMode.FOURS, stats)
        val threesStats = mapGameModeStats(BedwarsGameMode.THREES, stats)
        val doublesStats = mapGameModeStats(BedwarsGameMode.DOUBLES, stats)
        val solosStats = mapGameModeStats(BedwarsGameMode.SOLOS, stats)
        val fourVsFourStats = mapGameModeStats(BedwarsGameMode.FOURVFOUR, stats)

        return BedwarsPlayerData(
            displayName = playerName,
            lastUpdated = timeStamp,
            experience = experience,
            overallStats = overallStats,
            foursStats = foursStats,
            threesStats = threesStats,
            doublesStats = doublesStats,
            solosStats = solosStats,
            fourVsFourStats = fourVsFourStats
        )
    }

    private fun mapGameModeStats(gameMode: BedwarsGameMode, jsonData: JsonObject): GameModeData {
        val output = GameModeData()

        for ((statKey, fieldSetter) in statMappings) {
            val jsonKey = if (gameMode == BedwarsGameMode.OVERALL) {
                if (statKey == "winstreak") {
                    statKey
                } else {
                    "${statKey}_bedwars"
                }
            } else {
                if (statKey == "winstreak") {
                    "${gameMode.apiName}_$statKey"
                } else {
                    "${gameMode.apiName}_${statKey}_bedwars"
                }
            }
            val value = if (statKey == "winstreak") jsonData.getIntOr(jsonKey, -1) else jsonData.getIntOr(jsonKey)
            fieldSetter(output, value)
        }

        return output
    }

    private val statMappings: Map<String, (GameModeData, Int) -> Unit> = mapOf(
        "deaths" to { data, value -> data.deaths = value },
        "kills" to { data, value -> data.kills = value },
        "final_deaths" to { data, value -> data.finalDeaths = value },
        "final_kills" to { data, value -> data.finalKills = value },
        "beds_lost" to { data, value -> data.bedsLost = value },
        "beds_broken" to { data, value -> data.bedsBroken = value },
        "losses" to { data, value -> data.losses = value },
        "wins" to { data, value -> data.wins = value },
        "winstreak" to { data, value -> data.winstreak = value }
    )
}