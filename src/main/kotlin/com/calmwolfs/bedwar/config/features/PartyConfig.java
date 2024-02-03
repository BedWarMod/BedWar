package com.calmwolfs.bedwar.config.features;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDropdown;
import io.github.moulberry.moulconfig.annotations.ConfigEditorSlider;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class PartyConfig {
    @Expose
    @ConfigOption(name = "Party Match Stats", desc = "")
    @Accordion
    public MatchStats matchStats = new MatchStats();

    public static class MatchStats {
        @ConfigOption(name = "Enabled", desc = "At the conclusion of a game will list each player in your parties stats " +
                "for the game. §eWill show the nicked player twice (once with and once without nick")
        @Expose
        @ConfigEditorBoolean
        public boolean enabled = true;

        @Expose
        @ConfigOption(name = "Action Type", desc = "What the mod will do with your stats at the end of the game. " +
                "§eRequires above setting to be enabled.")
        @ConfigEditorDropdown(values = {"Nothing", "Copy to clipboard", "Send to party", "Copy and Send"})
        public int actionType = 0;

        @Expose
        @ConfigOption(name = "Stats Delay", desc = "The amount of time in milliseconds after the game ends before the " +
                "stats are sent in chat")
        @ConfigEditorSlider(minValue = 500, maxValue = 2500, minStep = 100)
        public int statsDelay = 1250;

        @ConfigOption(name = "Send On Loss", desc = "Will still send your stats if your team lost")
        @Expose
        @ConfigEditorBoolean
        public boolean sendOnLoss = false;

        @Expose
        @ConfigOption(name = "Send Delay", desc = "The amount of time in milliseconds after your parties stats are sent " +
                "in chat before you send your stats to the party")
        @ConfigEditorSlider(minValue = 50, maxValue = 1000, minStep = 50)
        public int sendDelay = 250;
    }

    @ConfigOption(name = "Party Commands", desc = "Shortens party commands and allows tab-completing for them. " +
            "\n§eCommands: /pt /pp /pko /pk /pd")
    @Expose
    @ConfigEditorBoolean
    public boolean shortCommands = true;

    @ConfigOption(name = "Party Kick Reason", desc = "Kick people while sending a reason when using §e/pk CalMWolfs low FKDR §7.")
    @Expose
    @ConfigEditorBoolean
    public boolean partyKickReason = true;
}