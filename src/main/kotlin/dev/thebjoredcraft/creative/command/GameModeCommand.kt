package dev.thebjoredcraft.creative.command

import dev.thebjoredcraft.creative.plugin
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents

object GameModeCommand {
    fun register() = plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
        event.registrar().register(Commands.literal("gamemode"))
    }
}