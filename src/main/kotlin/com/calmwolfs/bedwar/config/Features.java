package com.calmwolfs.bedwar.config;

import com.calmwolfs.bedwar.BedWarMod;
import com.calmwolfs.bedwar.config.features.AboutConfig;
import com.calmwolfs.bedwar.config.features.GuiConfig;
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

}