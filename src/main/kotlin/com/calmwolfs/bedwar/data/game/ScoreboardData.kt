package com.calmwolfs.bedwar.data.game

import com.calmwolfs.bedwar.events.game.ModTickEvent
import com.calmwolfs.bedwar.events.game.ScoreboardUpdateEvent
import com.calmwolfs.bedwar.utils.BedwarsUtils
import net.minecraft.client.Minecraft
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ScoreboardData {
    private var scoreboard = listOf<String>()
    private var objective = ""

    fun getScoreboard() = scoreboard.reversed()
    fun getObjective() = objective

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onTick(event: ModTickEvent) {
        if (!BedwarsUtils.inBedwarsArea && !event.repeatSeconds(1)) return
        var list = fetchScoreboardLines().take(15)
        list = formatLines(list)

        if (list != scoreboard) {
            scoreboard = list
            ScoreboardUpdateEvent(scoreboard.reversed(), objective).postAndCatch()
        }
    }

    private fun fetchScoreboardLines(): List<String> {
        val scoreboard = Minecraft.getMinecraft().theWorld?.scoreboard ?: return emptyList()
        val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return emptyList()
        this.objective = objective.displayName
        val scores = scoreboard.getSortedScores(objective)

        return scores.map {
            ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(it.playerName), it.playerName)
        }
    }

    private fun formatLines(rawList: List<String>): List<String> {
        val list = mutableListOf<String>()
        for (line in rawList) {
            val separator = splitIcons.find { line.contains(it) } ?: continue
            val split = line.split(separator)
            val start = split[0]
            var end = split[1]
            if (end.length >= 2) {
                end = end.substring(2)
            }
            var newLine = start + end
            newLine = newLine.toCharArray().filter { it.code in 21..126 || it.code == 167 || it.code == 0x2713 || it.code == 0x2717}.joinToString(separator = "")
            list.add(newLine)
        }
        return list
    }

    private val splitIcons = listOf(
        "\uD83C\uDF6B",
        "\uD83D\uDCA3",
        "\uD83D\uDC7D",
        "\uD83D\uDD2E",
        "\uD83D\uDC0D",
        "\uD83D\uDC7E",
        "\uD83C\uDF20",
        "\uD83C\uDF6D",
        "⚽",
        "\uD83C\uDFC0",
        "\uD83D\uDC79",
        "\uD83C\uDF81",
        "\uD83C\uDF89",
        "\uD83C\uDF82",
        "\uD83D\uDD2B"
    )
}