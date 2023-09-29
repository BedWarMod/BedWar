package com.calmwolfs.bedwar.events.game

import com.calmwolfs.bedwar.events.ModEvent

class ScoreboardUpdateEvent(val scoreboard: List<String>, val objective: String) : ModEvent()