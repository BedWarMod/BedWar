package com.calmwolfs.bedwar.data.types

import com.calmwolfs.bedwar.utils.computer.SimpleTimeMark

data class BedwarsPlayerData(
    val displayName: String = "-",
    val lastUpdated: SimpleTimeMark = SimpleTimeMark.farPast(),
    val experience: Int = -1,
    val overallStats: GameModeData = GameModeData(),
    val foursStats: GameModeData = GameModeData(),
    val threesStats: GameModeData = GameModeData(),
    val doublesStats: GameModeData = GameModeData(),
    val solosStats: GameModeData = GameModeData(),
    val fourVsFourStats: GameModeData = GameModeData()
)