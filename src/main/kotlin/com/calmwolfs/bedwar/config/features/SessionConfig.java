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

        @ConfigOption(name = "Show In BedWars Lobby", desc = "Display the session tracker while in a BedWars lobby")
        @Expose
        @ConfigEditorBoolean
        public boolean showInLobby = true;

        @Expose
        @ConfigOption(name = "Text Format", desc = "Drag text to change the appearance of the overlay.")
        @ConfigEditorDraggableList(exampleText = {
                // todo make these numbers actually make sense (get own numbers after session)
                "§e§lGame",
                "Kills: §a3",
                "Finals: §a5",
                "Beds: §a2",
                " ",
                "§e§lSession",
                "Kills: §a12 §7| §fKDR: §a1.47",
                "Finals: §a22 §7| §fFKDR: §a5.43",
                "Beds: §a14 §7| §fBBLR: §a4.46",
                "Wins: §a6 §7| §fWLR: §a3.22",
                " ",
                "Winstreak: §a3",
                "Session Games: §a7",
                "Session Time: §a32:47",
                "Game Time: §a5:31",
                "AVG Game Time: §a3:53",
        })
        public List<Integer> order = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));

        @Expose
        public Position position = new Position(10, 10, false, true);
    }
}