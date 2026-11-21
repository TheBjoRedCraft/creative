package dev.thebjoredcraft.creative.command

import dev.thebjoredcraft.creative.PREFIX
import dev.thebjoredcraft.creative.miniMessage
import dev.thebjoredcraft.creative.plugin
import dev.thebjoredcraft.creative.warps.WarpRegistry
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.math.pow
import kotlin.random.Random

object TeleportAwayCommand {
    private const val MIN_DISTANCE = 10_000.0
    private const val BORDER_MARGIN = 16.0

    fun register() =
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(
                Commands.literal("tpaway")
                    .requires { it.sender.hasPermission("creative.tpaway") }
                    .executes {
                        val player = it.source.sender as? Player

                        if (player == null) {
                            it.source.sender.sendRichMessage("$PREFIX <red>You must be a player to use this command.")
                            return@executes 1
                        }

                        val world = player.world
                        val currentLocation = player.location
                        val border = world.worldBorder

                        val center = border.center
                        val halfSize = border.size / 2.0

                        val minX = center.x - halfSize + BORDER_MARGIN
                        val maxX = center.x + halfSize - BORDER_MARGIN
                        val minZ = center.z - halfSize + BORDER_MARGIN
                        val maxZ = center.z + halfSize - BORDER_MARGIN

                        val warps = WarpRegistry.all().map { warp -> warp.location }

                        val target = (0 until 100).firstNotNullOfOrNull {
                            val x = Random.nextDouble(minX, maxX).toInt()
                            val z = Random.nextDouble(minZ, maxZ).toInt()

                            val tooCloseToPlayer =
                                (x - currentLocation.x).pow(2) +
                                        (z - currentLocation.z).pow(2) < MIN_DISTANCE.pow(2)

                            val tooCloseToWarp = warps.any { warp ->
                                warp.world == world &&
                                        (x - warp.x).pow(2) + (z - warp.z).pow(2) < MIN_DISTANCE.pow(
                                    2
                                )
                            }

                            if (!tooCloseToPlayer && !tooCloseToWarp) {
                                Location(world, x + 0.5, 0.0, z + 0.5)
                            } else {
                                null
                            }
                        }

                        if (target == null) {
                            player.sendRichMessage("$PREFIX <red>Could not find a suitable location.")
                            return@executes 1
                        }

                        player.sendActionBar(miniMessage.deserialize("Finding a destination..."))

                        world.getChunkAtAsync(
                            target.blockX shr 4,
                            target.blockZ shr 4,
                            true
                        ).thenAccept { _ ->
                            val y = world.getHighestBlockYAt(target.blockX, target.blockZ) + 1

                            val destination = Location(
                                world,
                                target.x,
                                y.toDouble(),
                                target.z
                            )

                            player.teleportAsync(destination).thenAccept { success ->
                                player.scheduler.execute(
                                    plugin,
                                    {
                                        if (success) {
                                            player.sendRichMessage("$PREFIX You have been teleported!")
                                        } else {
                                            player.sendRichMessage("$PREFIX <red>Teleportation failed.")
                                        }
                                    },
                                    null,
                                    1L
                                )
                            }
                        }

                        1
                    }
                    .build()
            )
        }
}