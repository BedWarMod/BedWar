package com.calmwolfs.bedwar.mixins.transformers;

import net.minecraft.util.ChatComponentText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChatComponentText.class)
public interface AccessorChatComponentText {
    @Accessor("text")
    void bedwar_setText(String text);

    @Accessor("text")
    String bedwar_text();
}