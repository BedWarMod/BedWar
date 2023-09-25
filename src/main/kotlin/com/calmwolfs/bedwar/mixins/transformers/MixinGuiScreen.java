package com.calmwolfs.bedwar.mixins.transformers;

import com.calmwolfs.bedwar.mixins.hooks.GuiScreenHookKt;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen extends Gui implements GuiYesNoCallback {
    @Inject(method = "handleComponentClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/event/ClickEvent;getAction()Lnet/minecraft/event/ClickEvent$Action;"), cancellable = true)
    private void blockComponentClick(CallbackInfoReturnable<Boolean> cir) {
        GuiScreenHookKt.onComponentClick(cir);
    }
}