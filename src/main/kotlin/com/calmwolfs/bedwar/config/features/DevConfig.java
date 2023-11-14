package com.calmwolfs.bedwar.config.features;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorText;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class DevConfig {
    @ConfigOption(name = "Use Old Api", desc = "Use the old system in case the current one is not working for you")
    @Expose
    @ConfigEditorBoolean
    public boolean oldApi = false;

    @Expose
    @ConfigOption(name = "Api Key", desc = "Put your api key here. §cDo not use this unless you really need this")
    @ConfigEditorText
    public String apiKey = "";
}