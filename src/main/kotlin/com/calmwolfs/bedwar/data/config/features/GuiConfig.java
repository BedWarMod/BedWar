package com.calmwolfs.bedwar.data.config.features;

import com.calmwolfs.bedwar.data.config.gui.GuiEditorManager;
import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorButton;
import io.github.moulberry.moulconfig.annotations.ConfigEditorKeybind;
import io.github.moulberry.moulconfig.annotations.ConfigEditorSlider;
import io.github.moulberry.moulconfig.annotations.ConfigOption;
import org.lwjgl.input.Keyboard;

public class GuiConfig {
    @ConfigOption(name = "Edit GUI Locations", desc = "Change the position of the mods overlays")
    @ConfigEditorButton(buttonText = "Edit")
    public Runnable positions = GuiEditorManager::openGuiPositionEditor;

    @Expose
    @ConfigOption(name = "Open Hotkey", desc = "Press this key to open the GUI Editor")
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_NONE)
    public int keyBindOpen = Keyboard.KEY_NONE;

    @Expose
    @ConfigOption(name = "Global GUI scale", desc = "Globally scale all mods GUIs")
    @ConfigEditorSlider(minValue = 0.1F, maxValue = 10, minStep = 0.05F)
    public float globalScale = 1F;
}
