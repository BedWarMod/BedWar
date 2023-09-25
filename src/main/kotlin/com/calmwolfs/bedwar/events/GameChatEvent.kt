package com.calmwolfs.bedwar.events

import net.minecraft.util.IChatComponent

class GameChatEvent(var message: String, var chatComponent: IChatComponent, var blockedReason: String = "") : ModEvent()