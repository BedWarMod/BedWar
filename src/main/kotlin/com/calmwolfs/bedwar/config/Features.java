package com.calmwolfs.bedwar.config;

import com.calmwolfs.bedwar.BedWarMod;
import com.calmwolfs.bedwar.config.features.*;
import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.Config;
import io.github.moulberry.moulconfig.annotations.Category;

public class Features extends Config {
    @Override
    public void saveNow() {
        BedWarMod.configManager.saveConfig("close-gui");
    }

    @Override
    public String getTitle() {
        return "BedWar Mod " + BedWarMod.getVersion() + " by §6CalMWolfs§r, config by §5Moulberry §rand §5nea89";
    }

    @Expose
    @Category(name = "About", desc = "Information about this BedWar Mod and its updates")
    public AboutConfig about = new AboutConfig();

    @Expose
    @Category(name = "GUI", desc = "Change the locations of GUI elements. (§e/bw gui§7)")
    public GuiConfig gui = new GuiConfig();

    @Expose
    @Category(name = "Inventory", desc = "Features related to your inventory and the shop")
    public InventoryConfig inventory = new InventoryConfig();

    @Expose
    @Category(name = "Session", desc = "Tracker for current session and current game")
    public SessionConfig session = new SessionConfig();

    @Expose
    @Category(name = "Party", desc = "Features relating to you being in a party")
    public PartyConfig party = new PartyConfig();

    @Expose
    @Category(name = "Chat", desc = "Any features relating to the chat")
    public ChatConfig chat = new ChatConfig();

    @Expose
    @Category(name = "Dev", desc = "Mostly developer features")
    public DevConfig dev = new DevConfig();
}