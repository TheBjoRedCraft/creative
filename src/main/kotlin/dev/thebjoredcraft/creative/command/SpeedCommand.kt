package dev.thebjoredcraft.creative.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import dev.thebjoredcraft.creative.PREFIX
import dev.thebjoredcraft.creative.plugin
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.entity.Player

object SpeedCommand {
    fun register() =
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(
                Commands.literal("speed")
                    .requires { it.sender.hasPermission("creative.speed") }
                    .executes {
                        val player = it.source.sender as? Player

                        if(player == null) {
                            it.source.sender.sendRichMessage("$PREFIX <red>You must be a player to use this command.")
                            return@executes 1
                        }


                        if(player.isFlying) {
                            player.flySpeed = 0.1f
                            player.sendRichMessage("$PREFIX Reset your fly speed.")
                        } else {
                            player.sendRichMessage("$PREFIX Reset your walk speed.")
                            player.walkSpeed = 0.2f
                        }
                        1
                    }
                    .then(Commands.argument("speed", IntegerArgumentType.integer()).executes {
                        val speed = it.getArgument("speed", Int::class.java)

                        if (speed !in 0..10) {
                            it.source.sender.sendRichMessage("$PREFIX <red>Speed must be between 0 and 10.")
                            return@executes 1
                        }

                        val player = it.source.sender as? Player

                        if (player == null) {
                            it.source.sender.sendRichMessage("$PREFIX <red>You must be a player to use this command.")
                            return@executes 1
                        }

                        if(player.isFlying) {
                            player.flySpeed = speed / 10f
                            player.sendRichMessage("$PREFIX Set your fly speed to $speed.")
                        } else {
                            player.walkSpeed = speed / 10f
                            player.sendRichMessage("$PREFIX Set your walk speed to $speed.")
                        }

                        1
                    })
                    .build())
        }
}