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

object GameModeCommand {
    fun register() =
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(
                Commands.literal("gamemode")
                    .requires { it.sender.hasPermission("creative.gamemode") }
                    .then(Commands.argument("gamemode", ArgumentTypes.gameMode()).executes {
                        val player = it.source.sender as? Player

                        if (player == null) {
                            it.source.sender.sendRichMessage("$PREFIX <red>You must be a player to use this command.")
                            return@executes 1
                        }

                        val gamemode = it.getArgument("gamemode", GameMode::class.java)

                        player.gameMode = gamemode
                        player.sendRichMessage("$PREFIX Set own gamemode to $COLOR<lang:${gamemode.translationKey()}><white>.")
                        1
                    })
                    .then(
                        Commands.argument("players", ArgumentTypes.players())
                            .requires { it.sender.hasPermission("creative.clear.others") }
                            .executes {
                                val players =
                                    it.getArgument(
                                        "players",
                                        PlayerSelectorArgumentResolver::class.java
                                    )
                                        .resolve(it.source)
                                val gamemode = it.getArgument("gamemode", GameMode::class.java)

                                players.forEach { player -> player.gameMode = gamemode }

                                it.source.sender.sendRichMessage("$PREFIX Updated the gamemode of $COLOR${players.size} <white>player(s) to $COLOR<lang:${gamemode.translationKey()}><white>.")
                                1
                            })
                    .build(),
                listOf("gm")
            )
        }
}