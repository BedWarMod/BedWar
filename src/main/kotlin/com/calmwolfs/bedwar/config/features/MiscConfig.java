package com.calmwolfs.bedwar.config.features;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class MiscConfig {
    @ConfigOption(name = "Copy Chat", desc = "Allows you to copy chat while control clicking it")
    @Expose
    @ConfigEditorBoolean
    public boolean copyChat = false;
}
