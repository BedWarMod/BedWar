package com.calmwolfs.bedwar.events

import net.minecraft.util.IChatComponent

class ModChatEvent(var message: String, var chatComponent: IChatComponent, var blockedReason: String = "") : ModEvent()