package com.calmwolfs.bedwar.config.features;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDropdown;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class PartyConfig {
    @Expose
    @ConfigOption(name = "Party Match Stats", desc = "")
    @Accordion
    public MatchStats matchStats = new MatchStats();

    public static class MatchStats {
        @ConfigOption(name = "Enabled", desc = "At the conclusion of a game will list each player in your parties stats for the game")
        @Expose
        @ConfigEditorBoolean
        public boolean enabled = true;

        @ConfigOption(name = "Show Solo", desc = "Will still show the stats even if you are not in a party")
        @Expose
        @ConfigEditorBoolean
        public boolean showSolo = true;

        @Expose
        @ConfigOption(name = "Action Type", desc = "What the mod will do with your stats at the end of the game. " +
                "§eRequires above setting to be enabled. §cAuto sending messages is use at your own risk")
        @ConfigEditorDropdown(values = {"Nothing", "Copy to clipboard", "Send to party", "Copy and Send"})
        public int actionType = 0;

        @ConfigOption(name = "Compressed Style", desc = "Will remove all the formatting for the above setting if enabled: Kills Finals Beds")
        @Expose
        @ConfigEditorBoolean
        public boolean compressed = true;

        @ConfigOption(name = "Send On Loss", desc = "Will still send your stats if your team lost")
        @Expose
        @ConfigEditorBoolean
        public boolean sendOnLoss = false;
    }

    @ConfigOption(name = "Party Commands", desc = "Shortens party commands and allows tab-completing for them. " +
            "\n§eCommands: /pt /pp /pko /pk")
    @Expose
    @ConfigEditorBoolean
    public boolean shortCommands = true;
}