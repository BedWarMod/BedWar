package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.events.GameChatEvent
import com.calmwolfs.bedwar.utils.StringUtils.matchMatcher
import com.calmwolfs.bedwar.utils.StringUtils.optionalPlural
import com.calmwolfs.bedwar.utils.StringUtils.removeResets
import com.calmwolfs.bedwar.utils.StringUtils.toPlayerName
import com.calmwolfs.bedwar.utils.StringUtils.trimWhiteSpaceAndResets
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object PartyUtils {
    val partyMembers = mutableListOf<String>()
    // todo stuff with nicks

    @SubscribeEvent
    fun onChat(event: GameChatEvent) {
        val message = event.message.trimWhiteSpaceAndResets().removeResets()

        // new member joined
        "§eYou have joined (?<name>.*)'s §eparty!".toPattern().matchMatcher(message) {
            val name = group("name").toPlayerName()
            if (!partyMembers.contains(name)) partyMembers.add(name)
        }
        "(?<name>.*) §ejoined the party.".toPattern().matchMatcher(message) {
            val name = group("name").toPlayerName()
            if (!partyMembers.contains(name)) partyMembers.add(name)
        }
        "§eYou'll be partying with: (?<names>.*)".toPattern().matchMatcher(message) {
            for (name in group("names").split(", ")) {
                val playerName = name.toPlayerName()
                if (!partyMembers.contains(playerName)) partyMembers.add(playerName)
            }
        }

        // one member got removed
        "(?<name>.*) §ehas left the party.".toPattern().matchMatcher(message) {
            val name = group("name").toPlayerName()
            partyMembers.remove(name)
        }
        "(?<name>.*) §ehas been removed from the party.".toPattern().matchMatcher(message) {
            val name = group("name").toPlayerName()
            partyMembers.remove(name)
        }
        "§eKicked (?<name>.*) because they were offline.".toPattern().matchMatcher(message) {
            val name = group("name").toPlayerName()
            partyMembers.remove(name)
        }
        "(?<name>.*) §ewas removed from your party because they disconnected.".toPattern().matchMatcher(message) {
            val name = group("name").toPlayerName()
            partyMembers.remove(name)
        }
        

        // party disbanded
        ".* §ehas disbanded the party!".toPattern().matchMatcher(message) {
            partyMembers.clear()
        }
        "§eYou have been kicked from the party by .* §e".toPattern().matchMatcher(message) {
            partyMembers.clear()
        }
        if (message == "§eYou left the party." ||
            message == "§cThe party was disbanded because all invites expired and the party was empty." ||
            message == "§cYou are not currently in a party."
        ) {
            partyMembers.clear()
        }

        // party list
        "Party (?:Leader|Moderators|Members): (?<names>.*)".toPattern().matchMatcher(message.unformat()) {
            for (name in group("names").split(" ● ")) {
                val playerName = name.replace(" ●", "").toPlayerName()
                if (playerName == Minecraft.getMinecraft().thePlayer.name) continue
                if (!partyMembers.contains(playerName)) partyMembers.add(playerName)
            }
        }
    }

    fun listMembers() {
        val size = partyMembers.size
        if (size == 0) {
            ModUtils.chat("§e[BedWar] No tracked party members!")
        } else {
            val memberLine = optionalPlural(size, "§atracked party member", "§atracked party members")
            ModUtils.chat("§a[BedWar] §7$memberLine§f:")
            for (member in partyMembers) {
                ModUtils.chat(" §a- §7$member")
            }
        }
    }
}