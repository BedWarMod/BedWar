package com.calmwolfs.bedwar.events.bedwars

import com.calmwolfs.bedwar.events.ModEvent

class KillEvent(val killer: String, val killed: String) : ModEvent()