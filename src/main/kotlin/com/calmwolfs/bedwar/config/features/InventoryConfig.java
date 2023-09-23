package com.calmwolfs.bedwar.config.features;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class InventoryConfig {

    @ConfigOption(name = "Middle Click", desc = "Middle clicks in shops to speed up buying")
    @Expose
    @ConfigEditorBoolean
    public boolean shopMiddleClick = true;

}
