package com.calmwolfs.bedwar.config.features;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class ChatConfig {
    @ConfigOption(name = "Copy Chat", desc = "Allows you to copy chat while control clicking it")
    @Expose
    @ConfigEditorBoolean
    public boolean copyChat = false;

    @ConfigOption(name = "Mention Notification", desc = "Play a ding sound when someone says your name in chat")
    @Expose
    @ConfigEditorBoolean
    public boolean chatMentions = true;

    @ConfigOption(name = "Highlight Own Name", desc = "Will make your own name yellow in chat when someone mentions it")
    @Expose
    @ConfigEditorBoolean
    public boolean chatHighlight = true;
}