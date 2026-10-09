package com.shrikerchesttree.nosuggestions.mixin;

import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.components.CommandSuggestions;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    // Проверка (она больше не нужна + засоряет логи)
    // @Inject(method = "keyPressed", at = @At("HEAD"))
    // private void debugKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
    //     System.out.println("ChatScreenMixin: keyPressed = " + keyCode);
    // }
    // @Inject(method = "keyPressed", at = @At("HEAD"))
    // private void debugSuggestions(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
    // System.out.println("CommandSuggestions.keyPressed");
    // }

        @Redirect(
        method = "keyPressed",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/CommandSuggestions;keyPressed(III)Z"
        )
    )
    private boolean disableCommandSuggestions(
            CommandSuggestions suggestions,
            int keyCode,
            int scanCode,
            int modifiers
    ) {
        return false;
    }
}