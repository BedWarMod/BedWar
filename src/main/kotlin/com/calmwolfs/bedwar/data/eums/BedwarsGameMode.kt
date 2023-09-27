package com.calmwolfs.bedwar.data.eums

enum class BedwarsGameMode(
    val displayName: String,
    val apiName: String,
    val shortName: String
) {
    OVERALL("Total", "", "Total"),
    FOURS("Fours", "four_four", "4s"),
    THREES("Threes", "four_three", "3s"),
    DOUBLES("Doubles", "eight_two", "2s"),
    SOLOS("Solos", "eight_one", "1s"),
    FOURVFOUR("4v4", "two_four", "4v4");

    companion object {
        fun getMapNames(): List<String> {
            return entries.filter { it != OVERALL }.map { "bedwars_${it.apiName}" }
        }
    }
}