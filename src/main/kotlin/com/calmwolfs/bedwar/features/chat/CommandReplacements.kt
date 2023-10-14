package com.calmwolfs.bedwar.features.chat

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.utils.BedwarsUtils
import com.calmwolfs.bedwar.utils.ChatUtils

object CommandReplacements {
    private val config get() = BedWarMod.feature.chat
    fun swapLobbyCommand() {
        if (!BedwarsUtils.inBedwarsLobby) return
        if (!config.swapLobbyCommand) return
        ChatUtils.sendCommandToServer("swaplobby 1")
    }
}