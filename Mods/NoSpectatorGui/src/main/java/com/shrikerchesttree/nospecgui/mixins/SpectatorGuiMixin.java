package com.shrikerchesttree.nospecgui.mixins;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpectatorGui.class)
public class SpectatorGuiMixin {

    // Хотбар с головами
    @Inject(
            method = "renderHotbar",
            at = @At("HEAD"),
            cancellable = true
    )
    private void disableSpectatorHotbar(
            GuiGraphics guiGraphics,
            CallbackInfo ci
    ) {
        ci.cancel();
    }

    // Текст
    @Inject(
            method = "renderTooltip",
            at = @At("HEAD"),
            cancellable = true
    )
    private void disableSpectatorTooltip(
            GuiGraphics guiGraphics,
            CallbackInfo ci
    ) {
        ci.cancel();
    }
}