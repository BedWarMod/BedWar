package com.calmwolfs.bedwar.config.features;

import com.calmwolfs.bedwar.config.gui.Position;
import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDraggableList;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SessionConfig {
    @Expose
    @ConfigOption(name = "Session Tracker", desc = "")
    @Accordion
    public SessionTracker sessionTracker = new SessionTracker();

    public static class SessionTracker {
        @ConfigOption(name = "Enabled", desc = "Display the session tracker overlay")
        @Expose
        @ConfigEditorBoolean
        public boolean enabled = true;

        @ConfigOption(name = "Show In BedWars Lobby", desc = "Also display the session tracker while in a BedWars lobby")
        @Expose
        @ConfigEditorBoolean
        public boolean showInLobby = true;

        @Expose
        @ConfigOption(name = "Text Format", desc = "Drag text to change the appearance of the overlay")
        @ConfigEditorDraggableList(exampleText = {
                "§e§lGame",
                "Kills: §a3",
                "Finals: §a5",
                "Beds: §a2",
                " ",
                "§e§lSession",
                "Kills: §a12 §7| §fKDR: §a0.59",
                "Finals: §a24 §7| §fFKDR: §a12",
                "Beds: §a7 §7| §fBBLR: §a3.5",
                "Wins: §a6 §7| §fWLR: §a3",
                " ",
                "Winstreak: §a3",
                "Win Rate: §a75.00%",
                "Session Games: §a8",
                "Session Time: §a53:03",
                "Game Time: §a11:58",
                "AVG Game Time: §a6:38",
        })
        public List<Integer> order = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16));

        @Expose
        public Position position = new Position(10, 50, false, true);
    }

    @Expose
    @ConfigOption(name = "Trap Queue", desc = "")
    @Accordion
    public TrapQueue trapQueue = new TrapQueue();

    public static class TrapQueue {
        @ConfigOption(name = "Enabled", desc = "Displays the current bought traps in the game in a gui")
        @Expose
        @ConfigEditorBoolean
        public boolean enabled = false;

        @Expose
        public Position position = new Position(120, 20, false, true);
    }
}