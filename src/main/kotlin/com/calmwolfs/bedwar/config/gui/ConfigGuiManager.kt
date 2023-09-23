package com.calmwolfs.bedwar.config.gui

import com.calmwolfs.bedwar.BedWarMod
import io.github.moulberry.moulconfig.gui.GuiScreenElementWrapper
import io.github.moulberry.moulconfig.gui.MoulConfigEditor

object ConfigGuiManager {
    val editor by lazy { MoulConfigEditor(ConfigManager.processor) }
    fun openConfigGui(search: String? = null) {
        if (search != null) {
            editor.search(search)
        }
        BedWarMod.screenToOpen = GuiScreenElementWrapper(editor)
    }
}