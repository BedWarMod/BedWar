package com.calmwolfs.bedwar.config.features;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorText;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class DevConfig {
    @Expose
    @ConfigOption(name = "Api Key", desc = "Put your api key here. §cDo not use this unless you are testing something")
    @ConfigEditorText
    public String apiKey = "";
}