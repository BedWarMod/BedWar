package com.calmwolfs.bedwar.events

import com.calmwolfs.bedwar.events.bedwars.*
import com.calmwolfs.bedwar.utils.BedwarsUtils
import com.calmwolfs.bedwar.utils.ModUtils
import com.calmwolfs.bedwar.utils.SoundUtils
import com.calmwolfs.bedwar.utils.StringUtils.removeResets
import com.calmwolfs.bedwar.utils.StringUtils.trimWhiteSpaceAndResets
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object BedwarsEventManager {
    private val gameEndPattern = "(?<team>\\w+)\\s+-(\\s+(?:\\[[^]]*]\\s+)?(\\w+)).".toPattern()
    private val bedBreakPattern = "BED DESTRUCTION > (?<team>\\w+) Bed .* (?:by|to) (?<player>\\w+)!".toPattern()
    private val finalKillPattern = "(?<killed>\\w+) .* (?<killer>\\w+)(?:.|'s final #.*.) FINAL KILL!".toPattern()
    private val killPattern = "§\\w(?<killed>\\w+) §7.* (?:by|for) §\\w(?<killer>\\w+)§7.".toPattern()
    private val voidPattern = "§\\w(?<killed>\\w+) §7fell into the void.".toPattern()
    private val teamEliminatedPattern = "TEAM ELIMINATED > (?<team>\\w+) Team has been eliminated!".toPattern()

    @SubscribeEvent
    fun onChat(event: ModChatEvent) {
        if (!BedwarsUtils.inBedwarsArea) return
        var message = event.message.trimWhiteSpaceAndResets()
        message = message.removeResets()

        println("trimmed: $message")

        if (message == "§e§lProtect your bed and destroy the enemy beds.") {
            StartGameEvent().postAndCatch()
        }

        var matcher = gameEndPattern.matcher(message.unformat())
        if (matcher.find()) {
            val matchedTeam = matcher.group("team")
            if (matchedTeam in teamColours) {
                EndGameEvent(matchedTeam).postAndCatch()
            }
        }

        // todo proper warning or remove thing
        if (message.unformat().startsWith("BED DESTRUCTION")) {
            matcher = bedBreakPattern.matcher(message.unformat())
            if (matcher.matches()) {
                var team = matcher.group("team")
                val player = matcher.group("player")
                if (team == "Your") team = BedwarsUtils.currentTeam

                BedBreakEvent(team, player).postAndCatch()
            } else {
                SoundUtils.playBeepSound()
                ModUtils.warning("Bed Break did not match")
            }
        }

        // todo proper warning or remove thing
        if (message.endsWith("§7. §b§lFINAL KILL!")) {
            matcher = finalKillPattern.matcher(message.unformat())
            if (matcher.matches()) {
                val killer = matcher.group("killer")
                val killed = matcher.group("killed")
                FinalKillEvent(killer, killed).postAndCatch()
            } else {
                SoundUtils.playBeepSound()
                ModUtils.warning("Final Kill did not match")
            }
        }

        matcher = killPattern.matcher(message)
        if (matcher.matches()) {
            val killer = matcher.group("killer")
            val killed = matcher.group("killed")
            KillEvent(killer, killed).postAndCatch()
        }

        matcher = voidPattern.matcher(message)
        if (matcher.matches()) {
            val killed = matcher.group("killed")
            KillEvent("-", killed).postAndCatch()
        }

        matcher = teamEliminatedPattern.matcher(message.unformat())
        if (matcher.matches()) {
            val team = matcher.group("team")
            TeamEliminatedEvent(team).postAndCatch()
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