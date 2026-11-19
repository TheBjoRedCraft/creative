package dev.thebjoredcraft.creative.warps

import dev.thebjoredcraft.creative.plugin
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents

object WarpCommand {
    fun register() =
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(
                Commands.literal("warps")
                    .requires { it.sender.hasPermission("creative.warps.list") }
                    .executes {
                        1
                    }
                    .build()
            )

            event.registrar().register(
                Commands.literal("setwarp")
                    .requires { it.sender.hasPermission("creative.warps.set") }
                    .executes {
                        1
                    }
                    .build()
            )

            event.registrar().register(
                Commands.literal("delwarp")
                    .requires { it.sender.hasPermission("creative.warps.delete") }
                    .executes {
                        1
                    }
                    .build()
            )

            event.registrar().register(
                Commands.literal("warp")
                    .requires { it.sender.hasPermission("creative.warps.teleport") }
                    .executes {
                        1
                    }
                    .build()
            )
        }
}