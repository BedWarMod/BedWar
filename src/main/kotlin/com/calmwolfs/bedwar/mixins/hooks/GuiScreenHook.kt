package com.calmwolfs.bedwar.mixins.hooks

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.utils.HypixelUtils
import com.calmwolfs.bedwar.utils.computer.KeyboardUtils
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun onComponentClick(cir: CallbackInfoReturnable<Boolean>) {
    if (HypixelUtils.onHypixel && BedWarMod.feature.misc.copyChat && KeyboardUtils.isControlKeyDown()) cir.returnValue = false
}