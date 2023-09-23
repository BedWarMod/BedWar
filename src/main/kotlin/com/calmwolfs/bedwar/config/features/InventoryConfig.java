package com.calmwolfs.bedwar.config.features;

import com.calmwolfs.bedwar.config.gui.Position;
import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class InventoryConfig {
    @Expose
    @ConfigOption(name = "Resource Overlay", desc = "")
    @Accordion
    public ResourceOverlay resourceOverlay = new ResourceOverlay();

    public static class ResourceOverlay {
        @ConfigOption(name = "Enabled", desc = "An overlay that shows how many of each resource you have")
        @Expose
        @ConfigEditorBoolean
        public boolean enabled = true;

        @ConfigOption(name = "Show Total", desc = "Adds another number that combines the count of each item you have" +
                "in your inventory and enderchest")
        @Expose
        @ConfigEditorBoolean
        public boolean showTotal = true;

        @Expose
        public Position position = new Position(-100, 10, false, true);
    }

    @ConfigOption(name = "Middle Click", desc = "Middle clicks in shops to speed up buying")
    @Expose
    @ConfigEditorBoolean
    public boolean shopMiddleClick = true;
}