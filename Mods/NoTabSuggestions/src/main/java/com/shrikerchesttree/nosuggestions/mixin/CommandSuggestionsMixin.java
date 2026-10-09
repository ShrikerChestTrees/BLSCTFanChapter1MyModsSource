package com.shrikerchesttree.nosuggestions.mixin;

import net.minecraft.client.gui.components.CommandSuggestions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandSuggestions.class)
public class CommandSuggestionsMixin {

    // Подсказки (окна)
    @Inject(method = "showSuggestions", at = @At("HEAD"), cancellable = true)
    private void disableShowSuggestions(boolean narrateFirstSuggestion, CallbackInfo ci) {
        ci.cancel(); // Отмена (т.е. выключает её, как и другие похожие строки)
    }

    // Обновление подсказки команд (только с никнеймами)
    @Inject(method = "updateCommandInfo", at = @At("HEAD"), cancellable = true)
    private void disableUpdateCommandInfo(CallbackInfo ci) {
        ci.cancel();
    }

    // Любые попытки скрыть/показать подсказки
    @Inject(method = "hide", at = @At("HEAD"), cancellable = true)
    private void disableHide(CallbackInfo ci) {
        ci.cancel();
    }

    // Клавиша Tab
    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void noTab(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        // GLFW.GLFW_KEY_TAB (я тогда затупил жёстко)
        if (keyCode == 258) {
            cir.setReturnValue(false); // Это тоже вырубает
        }
    }
}