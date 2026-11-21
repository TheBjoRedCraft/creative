package dev.thebjoredcraft.creative.world.generator.impl

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.generator.ChunkGenerator
import java.util.*

object VoidWorldGenerator : ChunkGenerator() {
    override fun canSpawn(world: World, x: Int, z: Int) = true
    override fun getFixedSpawnLocation(world: World, random: Random) =
        Location(world, 0.0, 100.0, 0.0)
}