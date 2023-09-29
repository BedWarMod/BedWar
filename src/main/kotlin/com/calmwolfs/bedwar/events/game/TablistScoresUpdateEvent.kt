package com.calmwolfs.bedwar.events.game

import com.calmwolfs.bedwar.events.ModEvent

class TablistScoresUpdateEvent(val scores: Map<String, Int>, val title: String) : ModEvent()