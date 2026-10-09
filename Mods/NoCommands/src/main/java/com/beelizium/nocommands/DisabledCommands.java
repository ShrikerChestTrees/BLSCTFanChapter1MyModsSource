package com.beelizium.nocommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import java.util.Iterator;

import static net.minecraft.commands.Commands.literal;

public class DisabledCommands implements ModInitializer {
    // Список удалённых команд
    private static final String[] toRemove = {
            "msg", "tell", "me", "balm", "citresewn", "closeguiscreen", "collective", "crafttweaker", "ct", "defaultoptions", "fmlayout", "fmvariable", "forcehorrormessage", "help", "mobcaps", "neoforge", "nomenublur", "oldpistons", "openguiscreen", "placebo", "random", "sc", "servercore", "teammsg", "tm", "tt20", "trigger", "voicechat", "w", "list"
    };

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            removeCommands(dispatcher);
            overrideBlocked(dispatcher);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(this::removeFromServer);
    }

    private void removeFromServer(MinecraftServer server) {
        CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();
        removeCommands(dispatcher);
        overrideBlocked(dispatcher);
    }

    private void removeCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        RootCommandNode<CommandSourceStack> root = dispatcher.getRoot();
        for (String cmd : toRemove) {
            Iterator<CommandNode<CommandSourceStack>> it = root.getChildren().iterator();
            while (it.hasNext()) {
                CommandNode<CommandSourceStack> node = it.next();
                if (node.getName().equalsIgnoreCase(cmd)) {
                    it.remove();
                    System.out.println("No /" + cmd);
                }
            }
        }
    }

    private void overrideBlocked(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String cmd : toRemove) {
            if (dispatcher.getRoot().getChild(cmd) == null) {
                continue;
            }

            dispatcher.register(
                    literal(cmd).executes(this::blockedCommand)
            );
        }
    }

    private int blockedCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        context.getSource().sendFailure(Component.literal("Эта команда отключена модом NoCommands"));
        return 0;
    }
}