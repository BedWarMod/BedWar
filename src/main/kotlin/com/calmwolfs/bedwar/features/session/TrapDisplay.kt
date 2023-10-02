package com.calmwolfs.bedwar.features.session

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.events.bedwars.BedBreakEvent
import com.calmwolfs.bedwar.events.bedwars.StartGameEvent
import com.calmwolfs.bedwar.events.game.GameChatEvent
import com.calmwolfs.bedwar.events.gui.GuiRenderEvent
import com.calmwolfs.bedwar.utils.BedwarsUtils
import com.calmwolfs.bedwar.utils.StringUtils.matchMatcher
import com.calmwolfs.bedwar.utils.StringUtils.removeResets
import com.calmwolfs.bedwar.utils.StringUtils.trimWhiteSpaceAndResets
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import com.calmwolfs.bedwar.utils.gui.GuiElementUtils.renderString
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class TrapDisplay {
    private val config get() = BedWarMod.feature.session.trapQueue

    private var trapLine = ""
    private val traps = mutableListOf<String>()
    private var hasBed = false

    @SubscribeEvent
    fun onGameStart(event: StartGameEvent) {
        traps.clear()
        hasBed = true
        formatDisplay()
    }

    // todo sync in the shop
    // todo rejoining

    @SubscribeEvent
    fun onBed(event: BedBreakEvent) {
        if (event.team == BedwarsUtils.currentTeamName) {
            hasBed = false
            formatDisplay()
        }
    }

    @SubscribeEvent
    fun onChat(event: GameChatEvent) {
        if (!BedwarsUtils.playingBedwars) return

        val message = event.message.trimWhiteSpaceAndResets().removeResets().unformat()

        // buying
        "(?<name>\\w+) purchased Miner Fatigue Trap".toPattern().matchMatcher(message) {
            traps.add("Miner Fatigue")
            formatDisplay()
            return
        }
        "(?<name>\\w+) purchased It's a trap!".toPattern().matchMatcher(message) {
            traps.add("It's a trap!")
            formatDisplay()
            return
        }
        "(?<name>\\w+) purchased Alarm Trap".toPattern().matchMatcher(message) {
            traps.add("Alarm Trap")
            formatDisplay()
            return
        }
        "(?<name>\\w+) purchased Counter-Offensive Trap".toPattern().matchMatcher(message) {
            traps.add("Counter-Offensive")
            formatDisplay()
            return
        }

        //losing
        "Alarm trap set off by \\w+ from \\w+ team!".toPattern().matchMatcher(message) {
            if (traps.isNotEmpty()) traps.removeAt(0)
            formatDisplay()
            return
        }
        if (message == "Miner Fatigue Trap was set off!" ||
            message == "It's a trap! was set off!" ||
            message == "Counter-Offensive Trap was set off!") {
            if (traps.isNotEmpty()) traps.removeAt(0)
            formatDisplay()
            return
        }
    }

    private fun formatDisplay() {
        val stringBuilder = StringBuilder()
        stringBuilder.append("§e§lTraps§7:")

        if (!hasBed) {
            stringBuilder.append(" §c§lYou Have No Bed!")
            trapLine = stringBuilder.toString()
            return
        }

        if (traps.isEmpty()) {
            stringBuilder.append(" §c§lYou Have No Traps!")
            trapLine = stringBuilder.toString()
            return
        }

        for (i in 0 until 3) {
            if (i < traps.size) {
                stringBuilder.append(" §6§l${traps[i]}")
            } else {
                stringBuilder.append(" §c§lNone")
            }
            if (i != 2) {
                stringBuilder.append(" §7>")
            }
        }

        trapLine = stringBuilder.toString()
    }

    @SubscribeEvent
    fun onRenderOverlay(event: GuiRenderEvent.GuiOverlayRenderEvent) {
        if (!config.enabled) return
        if (!BedwarsUtils.playingBedwars) return

        config.position.renderString(trapLine, posLabel = "Trap Display")
    }
}