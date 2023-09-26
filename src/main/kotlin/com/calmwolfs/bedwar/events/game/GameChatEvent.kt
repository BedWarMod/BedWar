package com.calmwolfs.bedwar.events.game

import com.calmwolfs.bedwar.events.ModEvent
import net.minecraft.util.IChatComponent

class GameChatEvent(var message: String, var chatComponent: IChatComponent, var blockedReason: String = "") : ModEvent()