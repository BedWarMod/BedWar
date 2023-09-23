package com.calmwolfs.bedwar.mixins.hooks

import com.calmwolfs.bedwar.events.gui.GuiContainerEvent
import net.minecraft.client.gui.inventory.GuiContainer
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

class GuiContainerHook(guiAny: Any) {
    val gui: GuiContainer

    init {
        gui = guiAny as GuiContainer
    }

    fun closeWindowPressed(ci: CallbackInfo) {
        if (GuiContainerEvent.CloseWindowEvent(gui, gui.inventorySlots).postAndCatch()) ci.cancel()
    }
}