package com.calmwolfs.bedwar.mixins.transformers;

import com.calmwolfs.bedwar.data.game.TooltipData;
import com.calmwolfs.bedwar.events.inventory.SlotClickEvent;
import com.calmwolfs.bedwar.mixins.hooks.GuiContainerHook;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiContainer.class)
public abstract class MixinGuiContainer extends GuiScreen {
    @Shadow
    private Slot theSlot;
    @Unique
    private final GuiContainerHook bedwar_hook = new GuiContainerHook(this);

    @Inject(method = "keyTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V", shift = At.Shift.BEFORE), cancellable = true)
    private void closeWindowPressed(CallbackInfo ci) {
        bedwar_hook.closeWindowPressed(ci);
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V", ordinal = 1))
    private void backgroundDrawn(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        bedwar_hook.backgroundDrawn(mouseX, mouseY, partialTicks);
    }

    @Inject(method = "handleMouseClick", at = @At(value = "HEAD"), cancellable = true)
    public void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
        if (slotIn == null) return;
        GuiContainer $this = (GuiContainer) (Object) this;
        SlotClickEvent event = new SlotClickEvent($this, slotIn, slotId, clickedButton, clickType);
        event.postAndCatch();
        if (event.isCanceled()) {
            ci.cancel();
            return;
        }
        if (event.getUsePickblock()) {
            $this.mc.playerController.windowClick($this.inventorySlots.windowId, slotId, 2, 3, $this.mc.thePlayer);
            ci.cancel();
        }
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V", shift = At.Shift.AFTER))
    public void drawScreen_after(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        TooltipData.Companion.setLastSlot(theSlot);
    }
}