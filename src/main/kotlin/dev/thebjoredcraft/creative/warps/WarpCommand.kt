package dev.thebjoredcraft.creative.warps

import dev.thebjoredcraft.creative.plugin
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents

object WarpCommand {
    fun register() =
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(
                Commands.literal("warps")
                    .build()
            )

            event.registrar().register(
                Commands.literal("setwarp")
                    .build()
            )

            event.registrar().register(
                Commands.literal("delwarp")
                    .build()
            )

            event.registrar().register(
                Commands.literal("warp")
                    .build()
            )
        }
}