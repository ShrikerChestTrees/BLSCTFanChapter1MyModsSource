package com.beeshield.dark_damage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.*;

public class DarkDamage implements ModInitializer {
    private static final Map<ServerPlayerEntity, Integer> darkTicks = new HashMap<>();
    private static final Map<ServerPlayerEntity, Boolean> wasInDarkness = new HashMap<>();
    private static final Map<ServerPlayerEntity, Integer> lightMessageDelay = new HashMap<>();
    private static final Random random = new Random();

    // Случайные сообщения при тёмной зоне
    private static final String[] DARK_MESSAGES = {
            "§7Кажется вы кого-то слышали...",
            "§7Вам не по себе...",
            "§7Вы не хотите оставаться в тьме...",
            "§7Вы потихоньку сходите с ума...",
            "§7Вы ищите свет...",
            "§7Кажется вы кого-то видите...",
            "§7Вы хотите уйти отсюда..."
    };

    // Случайные сообщения при светлой зоне
    private static final String[] LIGHT_MESSAGES = {
            "§7Наконец-то свет!",
            "§7Свобода!",
            "§7Вы ощущаете прилив сил.",
            "§7Свет!",
            "§7Выбрался из тьмы!",
            "§7Ура! Свет!",
            "§7Вы нашли свет."
    };

    @Override
    public void onInitialize() {
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            darkTicks.remove(oldPlayer);
            wasInDarkness.remove(oldPlayer);
            lightMessageDelay.remove(oldPlayer);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                int light = player.getWorld().getLightLevel(player.getBlockPos());

                if (light <= 3) {
                    int ticks = darkTicks.getOrDefault(player, 0) + 1;
                    darkTicks.put(player, ticks);
                    wasInDarkness.put(player, true);
                    lightMessageDelay.put(player, 0);

                    // Урон
                    if (ticks == 50 || (ticks > 50 && (ticks - 50) % 25 == 0)) {
                        player.damage(player.getWorld().getDamageSources().generic(), 2.0F);
                    }

                    if (ticks % 50 == 0) {
                        String msg = DARK_MESSAGES[random.nextInt(DARK_MESSAGES.length)];
                        player.sendMessage(Text.literal(msg), false);
                    }
                } else {
                    int delay = lightMessageDelay.getOrDefault(player, 0);
                    if (wasInDarkness.getOrDefault(player, false)) {
                        int dt = darkTicks.getOrDefault(player, 0);
                        int waitTicks = 0;
                        if (dt < 50) {
                            waitTicks = 50 - dt;
                        } else {
                            int mod = (dt - 50) % 25;
                            waitTicks = (mod == 0) ? 25 : 25 - mod;
                        }
                        // Задержка при светлой
                        lightMessageDelay.put(player, waitTicks);
                        wasInDarkness.put(player, false);
                        darkTicks.put(player, 0);
                        delay = waitTicks;
                    }

                    if (delay > 0) {
                        lightMessageDelay.put(player, delay - 1);
                        if (delay - 1 == 0) {
                            String msg = LIGHT_MESSAGES[random.nextInt(LIGHT_MESSAGES.length)];
                            player.sendMessage(Text.literal(msg), false);
                        }
                    }
                }
            }
        });
    }
}
