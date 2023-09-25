package com.calmwolfs.bedwar.events

import net.minecraft.util.IChatComponent

class PlayerChatEvent(var message: String, var chatComponent: IChatComponent, val sender: String, var blockedReason: String = "") : ModEvent()