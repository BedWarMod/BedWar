package com.calmwolfs.bedwar.config;

import com.calmwolfs.bedwar.BedWarMod;
import com.calmwolfs.bedwar.config.features.AboutConfig;
import com.calmwolfs.bedwar.config.features.ChatConfig;
import com.calmwolfs.bedwar.config.features.DevConfig;
import com.calmwolfs.bedwar.config.features.GuiConfig;
import com.calmwolfs.bedwar.config.features.InventoryConfig;
import com.calmwolfs.bedwar.config.features.NotificationsConfig;
import com.calmwolfs.bedwar.config.features.PartyConfig;
import com.calmwolfs.bedwar.config.features.SessionConfig;
import com.calmwolfs.bedwar.config.features.TeamConfig;
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
    @Category(name = "About", desc = "Information about this mod and its updates")
    public AboutConfig about = new AboutConfig();

    @Expose
    @Category(name = "GUI", desc = "Change the locations and the appearance of GUI elements")
    public GuiConfig gui = new GuiConfig();

    @Expose
    @Category(name = "Inventory", desc = "Features related to your inventory and the item shop")
    public InventoryConfig inventory = new InventoryConfig();

    @Expose
    @Category(name = "Session", desc = "Trackers for your current session and current game")
    public SessionConfig session = new SessionConfig();

    @Expose
    @Category(name = "Party", desc = "Features relating to you being in a party")
    public PartyConfig party = new PartyConfig();

    @Expose
    @Category(name = "Team", desc = "Features relating to you and your team")
    public TeamConfig team = new TeamConfig();

    @Expose
    @Category(name = "Chat", desc = "Any features relating to the chat")
    public ChatConfig chat = new ChatConfig();

    @Expose
    @Category(name = "Notifications", desc = "Notifications that are displayed based on various conditions")
    public NotificationsConfig notifications = new NotificationsConfig();

    @Expose
    @Category(name = "Dev", desc = "Mostly developer features")
    public DevConfig dev = new DevConfig();

    @Expose
    public Storage storage = new Storage();
}