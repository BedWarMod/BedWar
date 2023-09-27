package com.calmwolfs.bedwar.events.game

import com.calmwolfs.bedwar.events.ModEvent
import net.minecraft.util.IChatComponent

class GameChatEvent(var message: String, var component: IChatComponent, var blockedReason: String = "") : ModEvent()