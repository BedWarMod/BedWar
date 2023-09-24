package com.calmwolfs.bedwar.config.features;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class PartyConfig {
    @Expose
    @ConfigOption(name = "Party Match Stats", desc = "")
    @Accordion
    public MatchStats matchStats = new MatchStats();

    public static class MatchStats {
        @ConfigOption(name = "Enabled", desc = "At the conclusion of a game will list each player in your parties stats fro the game")
        @Expose
        @ConfigEditorBoolean
        public boolean enabled = true;

        @ConfigOption(name = "Show Solo", desc = "Will still show the stats even if you are not in a party")
        @Expose
        @ConfigEditorBoolean
        public boolean showSolo = true;
    }
}
