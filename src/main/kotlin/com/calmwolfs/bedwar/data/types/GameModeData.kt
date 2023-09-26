package com.calmwolfs.bedwar.data.types

data class GameModeData(
    var deaths: Int = 0,
    var kills: Int = 0,
    var finalDeaths: Int = 0,
    var finalKills: Int = 0,
    var bedsLost: Int = 0,
    var bedsBroken: Int = 0,
    var losses: Int = 0,
    var wins: Int = 0,
    var winstreak: Int = 0
)