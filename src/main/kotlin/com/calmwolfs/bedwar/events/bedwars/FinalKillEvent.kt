package com.calmwolfs.bedwar.events.bedwars

import com.calmwolfs.bedwar.events.ModEvent

class FinalKillEvent(val killer: String, val killed: String) : ModEvent()