package com.calmwolfs.bedwar.mixins;

import com.calmwolfs.bedwar.config.gui.GuiEditorManager;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "updateCameraAndRender", at = @At("TAIL"))
    private void onLastRender(float partialTicks, long nanoTime, CallbackInfo ci) {
        GuiEditorManager.renderLast();
    }
}