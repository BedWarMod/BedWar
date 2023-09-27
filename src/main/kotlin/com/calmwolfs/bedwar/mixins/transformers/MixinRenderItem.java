package com.calmwolfs.bedwar.mixins.transformers;

import com.calmwolfs.bedwar.mixins.hooks.RenderItemHookKt;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public abstract class MixinRenderItem {
    @Inject(method = "renderItemIntoGUI", at = @At("RETURN"))
    public void renderItemReturn(ItemStack stack, int x, int y, CallbackInfo ci) {
        RenderItemHookKt.renderItemReturn(stack, x, y);
    }
}