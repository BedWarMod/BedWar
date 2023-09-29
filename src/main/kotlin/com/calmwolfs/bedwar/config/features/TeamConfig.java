package com.calmwolfs.bedwar.config.features;

import com.calmwolfs.bedwar.config.gui.Position;
import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class TeamConfig {
    @Expose
    @ConfigOption(name = "Team Status Display", desc = "")
    @Accordion
    public TeamStatus teamStatus = new TeamStatus();

    public static class TeamStatus {
        @ConfigOption(name = "Enabled", desc = "Display the current health of all your teammates and how long it will take them to respawn")
        @Expose
        @ConfigEditorBoolean
        public boolean enabled = true;

        @ConfigOption(name = "Show Solo", desc = "Will still display if you are playing solos or had no teammates")
        @Expose
        @ConfigEditorBoolean
        public boolean showSolo = true;

        @Expose
        public Position position = new Position(-310, 10, false, true);
    }
}