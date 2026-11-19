package dev.thebjoredcraft.creative.command

import dev.thebjoredcraft.creative.COLOR
import dev.thebjoredcraft.creative.PREFIX
import dev.thebjoredcraft.creative.plugin
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.GameMode
import org.bukkit.entity.Player

object ClearCommand {
    fun register() =
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(
                Commands.literal("clear")
                    .executes {
                        val player = it.source.sender as? Player

                        if (player == null) {
                            it.source.sender.sendRichMessage("$PREFIX <red>You must be a player to use this command.")
                            return@executes 1
                        }

                        val items =
                            player.inventory.contents.filterNotNull().sumOf { item -> item.amount }

                        player.inventory.clear()
                        player.sendRichMessage("$PREFIX Cleared $COLOR$items <white>item(s) from your inventory.")
                        1
                    }
                    .then(Commands.argument("players", ArgumentTypes.players()).executes {
                        val players =
                            it.getArgument("players", PlayerSelectorArgumentResolver::class.java)
                                .resolve(it.source)

                        players.forEach { player -> player.inventory.clear() }

                        it.source.sender.sendRichMessage("$PREFIX Cleared the inventory of $COLOR${players.size} <white>player(s).")
                        1
                    })
                    .build()
            )
        }
}