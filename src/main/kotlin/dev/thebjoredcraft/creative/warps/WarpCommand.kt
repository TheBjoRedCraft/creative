package dev.thebjoredcraft.creative.warps

import com.mojang.brigadier.arguments.StringArgumentType
import dev.thebjoredcraft.creative.COLOR
import dev.thebjoredcraft.creative.PREFIX
import dev.thebjoredcraft.creative.miniMessage
import dev.thebjoredcraft.creative.plugin
import dev.thebjoredcraft.creative.warps.config.WarpConfigManager
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

object WarpCommand {
    fun register() =
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(
                Commands.literal("warps")
                    .requires { it.sender.hasPermission("creative.warps.list") }
                    .executes {
                        val warps = WarpRegistry.all()

                        if (warps.isEmpty()) {
                            it.source.sender.sendRichMessage("$PREFIX <red>There are no warps set.")
                            return@executes 1
                        }

                        val warpList =
                            warps.joinToString(", ") { warp -> "$COLOR<click:run_command:'/warp ${warp.name}'>${warp.name}</click><white>" }

                        it.source.sender.sendRichMessage("$PREFIX There are$COLOR ${warps.size} <white>Warps: $warpList")

                        1
                    }
                    .build()
            )

            val nameRegex = Regex("^[a-zA-Z0-9_-]{1,16}$")

            event.registrar().register(
                Commands.literal("setwarp")
                    .requires { it.sender.hasPermission("creative.warps.set") }
                    .then(
                        Commands.argument("name", StringArgumentType.word()).executes {
                            val player = it.source.sender as? Player

                            if (player == null) {
                                it.source.sender.sendRichMessage("$PREFIX <red>You must be a player to use this command.")
                                return@executes 1
                            }

                            val location = player.location
                            val name = it.getArgument("name", String::class.java)

                            if (!name.matches(nameRegex)) {
                                it.source.sender.sendRichMessage("$PREFIX <red>Warp name must be 1-16 characters long and can only contain letters, numbers, underscores, and hyphens.")
                                return@executes 1
                            }

                            WarpRegistry.register(name, location, player.name)
                            WarpConfigManager.saveAll()

                            it.source.sender.sendRichMessage("$PREFIX Created warp $COLOR$name <white>at your current location.")
                            1
                        })
                    .build()
            )

            event.registrar().register(
                Commands.literal("delwarp")
                    .requires { it.sender.hasPermission("creative.warps.delete") }
                    .then(
                        Commands.argument("name", StringArgumentType.word())
                            .suggests { _, builder ->
                                WarpRegistry.all().forEach { warp -> builder.suggest(warp.name) }
                                builder.buildFuture()
                            }.executes {
                                val name = it.getArgument("name", String::class.java)

                                WarpRegistry.unregister(name)
                                WarpConfigManager.saveAll()

                                it.source.sender.sendRichMessage("$PREFIX Deleted warp $COLOR$name<white>.")
                                1
                            })
                    .build()
            )

            event.registrar().register(
                Commands.literal("warp")
                    .requires { it.sender.hasPermission("creative.warps.teleport") }
                    .then(
                        Commands.argument("name", StringArgumentType.word())
                            .suggests { _, builder ->
                                WarpRegistry.all().forEach { warp -> builder.suggest(warp.name) }
                                builder.buildFuture()
                            }.executes {
                                val player = it.source.sender as? Player

                                if (player == null) {
                                    it.source.sender.sendRichMessage("$PREFIX <red>You must be a player to use this command.")
                                    return@executes 1
                                }

                                val name = it.getArgument("name", String::class.java)
                                val warp = WarpRegistry.getWarp(name)

                                if (warp == null) {
                                    it.source.sender.sendRichMessage("$PREFIX <red>Warp $COLOR$name <red>does not exist.")
                                    return@executes 1
                                }

                                it.source.sender.sendActionBar(miniMessage.deserialize("Teleporting to warp $COLOR$name<white>..."))

                                player.teleportAsync(warp.location).thenRun {
                                    it.source.sender.sendActionBar(Component.empty())
                                    it.source.sender.sendRichMessage("$PREFIX Teleported to warp $COLOR$name<white>.")
                                    it.source.sender.playSound(
                                        Sound.sound(
                                            org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT,
                                            Sound.Source.MASTER, 1f, 1f
                                        ), Sound.Emitter.self()
                                    )
                                }
                                1
                            })
                    .build()
            )
        }
}