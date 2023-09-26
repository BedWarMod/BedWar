package com.calmwolfs.bedwar.events.game

import com.calmwolfs.bedwar.events.ModEvent
import net.minecraft.util.IChatComponent

class PlayerChatEvent(var message: String, var chatComponent: IChatComponent, val sender: String, var blockedReason: String = "") : ModEvent()