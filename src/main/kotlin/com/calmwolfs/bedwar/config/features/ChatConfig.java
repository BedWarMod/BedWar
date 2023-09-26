package com.calmwolfs.bedwar.config.features;

import com.calmwolfs.bedwar.data.eums.BedwarsGameMode;
import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDropdown;
import io.github.moulberry.moulconfig.annotations.ConfigOption;
import io.github.moulberry.moulconfig.observer.Property;

public class ChatConfig {
    @ConfigOption(name = "Copy Chat", desc = "Allows you to copy chat while control clicking it")
    @Expose
    @ConfigEditorBoolean
    public boolean copyChat = false;

    @ConfigOption(name = "Mention Notification", desc = "Play a ding sound when someone says your name in chat")
    @Expose
    @ConfigEditorBoolean
    public boolean chatMentions = true;

    @ConfigOption(name = "Highlight Own Name", desc = "Will make your own name yellow in chat when someone mentions it")
    @Expose
    @ConfigEditorBoolean
    public boolean chatHighlight = true;

    @Expose
    @ConfigOption(name = "Player Stats", desc = "")
    @Accordion
    public PlayerStats playerStats = new PlayerStats();

    public static class PlayerStats {
        @ConfigOption(name = "Default Mode", desc = "What game mode to default to when showing in chat. §eCommand: /bws <name> <mode>")
        @Expose
        @ConfigEditorDropdown
        public Property<BedwarsGameMode> statType = Property.of(BedwarsGameMode.OVERALL);

        @ConfigOption(name = "Show on Join", desc = "Shows the stats of a player when they join your party. " +
                "Will also use the above setting as the default")
        @Expose
        @ConfigEditorBoolean
        public boolean partyJoin = false;
    }
}