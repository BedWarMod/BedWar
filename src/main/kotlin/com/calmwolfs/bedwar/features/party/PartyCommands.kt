package com.calmwolfs.bedwar.features.party

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.utils.ChatUtils
import com.calmwolfs.bedwar.utils.PartyUtils

object PartyCommands {
    private val config get() = BedWarMod.feature.party

    fun kickOffline() {
        if (!isEnabled()) return
        ChatUtils.sendCommandToServer("party kickoffline")
    }

    fun warp() {
        if (!isEnabled()) return
        ChatUtils.sendCommandToServer("party warp")
    }

    fun disband() {
        if (!isEnabled()) return
        ChatUtils.sendCommandToServer("party disband")
    }

    fun kick(args: Array<String>) {
        if (!isEnabled()) return
        if (args.isEmpty()) return
        if (args.size > 1 && config.partyKickReason) {
            ChatUtils.sendCommandToServer("pc Kicking ${args[0]}: ${args.drop(1).joinToString(" ").trim()}")
        }
        ChatUtils.sendCommandToServer("party kick ${args[0]}")
    }

    fun transfer(args: Array<String>) {
        if (args.isEmpty()) ChatUtils.sendCommandToServer("pt")
        if (!isEnabled()) return
        ChatUtils.sendCommandToServer("party transfer ${args[0]}")
    }

    fun promote(args: Array<String>) {
        if (!isEnabled()) return
        if (args.isEmpty()) return
        ChatUtils.sendCommandToServer("party promote ${args[0]}")
    }

    val otherPartyCommands = listOf(
        "Disband",
        "KickOffline",
        "Leave",
        "List",
        "Mute",
        "Private",
        "Warp",
        "Settings"
    )

    private fun isEnabled() = config.shortCommands && PartyUtils.partyMembers.isNotEmpty()
}