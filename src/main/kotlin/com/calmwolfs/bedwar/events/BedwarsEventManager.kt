package com.calmwolfs.bedwar.events

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.data.jsonobjects.ChatRegexJson
import com.calmwolfs.bedwar.events.bedwars.*
import com.calmwolfs.bedwar.events.game.GameChatEvent
import com.calmwolfs.bedwar.features.team.TeamStatus
import com.calmwolfs.bedwar.utils.BedwarsUtils
import com.calmwolfs.bedwar.utils.ModUtils
import com.calmwolfs.bedwar.utils.SoundUtils
import com.calmwolfs.bedwar.utils.StringUtils.findMatcher
import com.calmwolfs.bedwar.utils.StringUtils.matchMatcher
import com.calmwolfs.bedwar.utils.StringUtils.removeResets
import com.calmwolfs.bedwar.utils.StringUtils.trimWhiteSpaceAndResets
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object BedwarsEventManager {
    private var gameEndPattern = "".toPattern()
    private var bedBreakPattern = "".toPattern()
    private var finalKillPattern = "".toPattern()
    private var killPattern = "".toPattern()
    private var selfKillPattern = "".toPattern()
    private var teamEliminatedPattern = "".toPattern()

    @SubscribeEvent
    fun onChat(event: GameChatEvent) {
        if (!BedwarsUtils.inBedwarsArea) return
        val message = event.message.trimWhiteSpaceAndResets().removeResets()

        if (message == "§e§lProtect your bed and destroy the enemy beds.") {
            StartGameEvent().postAndCatch()
            return
        }

        if (!BedwarsUtils.inBedwarsGame) return
        val unformatted = message.unformat()

        println("trimmed: $message")

        "(?<player>\\w+) disconnected.".toPattern().matchMatcher(unformatted) {
            TeamStatus.playerDisconnect(group("player"))
        }

        "(?<player>\\w+) reconnected.".toPattern().matchMatcher(unformatted) {
            TeamStatus.playerReconnect(group("player"))
        }

        killPattern.findMatcher(message) {
            val killer = group("killer")
            val killed = group("killed")
            KillEvent(killer, killed).postAndCatch()
            return
        }

        selfKillPattern.findMatcher(message) {
            val killed = group("killed")
            if (message.endsWith("§7. §b§lFINAL KILL!")) {
                FinalKillEvent("-", killed).postAndCatch()
            }
            KillEvent("-", killed).postAndCatch()
            return
        }

        var matcher = gameEndPattern.matcher(unformatted)
        if (matcher.find()) {
            val matchedTeam = matcher.group("team")
            if (matchedTeam in teamColours) {
                EndGameEvent(matchedTeam).postAndCatch()
                return
            }
        }

        if (message.endsWith("§7. §b§lFINAL KILL!")) {
            matcher = finalKillPattern.matcher(unformatted)
            if (matcher.find()) {
                val killer = matcher.group("killer")
                val killed = matcher.group("killed")
                FinalKillEvent(killer, killed).postAndCatch()
            } else {
                SoundUtils.playBeepSound()
                ModUtils.warning("Final Kill message did not match!\n" +
                        "Please report this final kill message on the github so your stats can be more accurate :)")
            }
            return
        }

        if (message.unformat().startsWith("BED DESTRUCTION")) {
            matcher = bedBreakPattern.matcher(unformatted)
            if (matcher.find()) {
                var team = matcher.group("team")
                val player = matcher.group("player")
                if (team == "Your") team = BedwarsUtils.currentTeamName
                BedBreakEvent(team, player).postAndCatch()
            } else {
                SoundUtils.playBeepSound()
                ModUtils.warning("Bed Break message did not match!\n" +
                        "Please report this bed break message on the github so your stats can be more accurate :)")
            }
            return
        }

        teamEliminatedPattern.matchMatcher(unformatted) {
            val team = group("team")
            TeamEliminatedEvent(team).postAndCatch()
            return
        }
    }

    @SubscribeEvent
    fun onRepoReload(event: RepositoryReloadEvent) {
        try {
            val data = event.getConstant<ChatRegexJson>("ChatRegex") ?: return
            gameEndPattern = data.gameEndPattern.toPattern()
            bedBreakPattern = data.bedBreakPattern.toPattern()
            finalKillPattern = data.finalKillPattern.toPattern()
            killPattern = data.killPattern.toPattern()
            selfKillPattern = data.selfKillPattern.toPattern()
            teamEliminatedPattern = data.teamEliminatedPattern.toPattern()
            BedWarMod.repo.successfulConstants.add("ChatRegex")
        } catch (e: Exception) {
            ModUtils.error("Error in repository reload event (ChatRegex)")
            BedWarMod.repo.unsuccessfulConstants.add("ChatRegex")
        }
    }

    private val teamColours = listOf(
        "Red",
        "Blue",
        "Green",
        "Yellow",
        "Aqua",
        "White",
        "Pink",
        "Gray"
    )
}