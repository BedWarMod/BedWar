package com.calmwolfs.bedwar.config.features;

import com.calmwolfs.bedwar.config.gui.Position;
import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorSlider;
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

        @ConfigOption(name = "Show Total", desc = "Also displays the total of each resource")
        @Expose
        @ConfigEditorBoolean
        public boolean showTotal = true;

        @Expose
        public Position position = new Position(-100, 50, false, true);
    }

    @Expose
    @ConfigOption(name = "Shop Inventory Overlay", desc = "")
    @Accordion
    public ShopInventoryOverlay shopInventoryOverlay = new ShopInventoryOverlay();

    public static class ShopInventoryOverlay {
        @ConfigOption(name = "Enabled", desc = "Enable highlighting slots based on conditions")
        @Expose
        @ConfigEditorBoolean
        public boolean enabled = false;

        @Expose
        @ConfigOption(name = "Hide Already Purchased", desc = "Greys out items that you cannot purchase because you already bought them")
        @ConfigEditorBoolean
        public boolean hidePurchased = true;

        @Expose
        @ConfigOption(name = "Block Clicks", desc = "Block the clicks on already purchased items to speed up time in the shop")
        @ConfigEditorBoolean
        public boolean blockClicks = false;

        @Expose
        @ConfigOption(name = "Purchased Opacity", desc = "How strong should the items be greyed out?")
        @ConfigEditorSlider(minValue = 0, maxValue = 255, minStep = 5)
        public int opacityGrey = 210;

        @Expose
        @ConfigOption(name = "Display Affordable", desc = "Adds a green background behind all the items in the shop that you can afford")
        @ConfigEditorBoolean
        public boolean showAffordable = false;

        @Expose
        @ConfigOption(name = "Affordable Opacity", desc = "How green should the affordable items be?")
        @ConfigEditorSlider(minValue = 0, maxValue = 255, minStep = 5)
        public int opacityGreen = 45;
    }

    @ConfigOption(name = "Middle Click", desc = "Middle clicks in shops to stop items sticking to your cursor")
    @Expose
    @ConfigEditorBoolean
    public boolean shopMiddleClick = false;
}