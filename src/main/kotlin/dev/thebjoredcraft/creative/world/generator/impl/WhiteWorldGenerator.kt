package dev.thebjoredcraft.creative.world.generator.impl

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import java.util.*

object WhiteWorldGenerator : ChunkGenerator() {
    override fun canSpawn(world: World, x: Int, z: Int) = true

    override fun getFixedSpawnLocation(world: World, random: Random) =
        Location(world, 0.0, 100.0, 0.0)

    override fun generateNoise(
        worldInfo: WorldInfo,
        random: Random,
        chunkX: Int,
        chunkZ: Int,
        chunkData: ChunkData
    ) {
        for (y in 51..99) {
            for (x in 0..15) {
                for (z in 0..15) {
                    chunkData.setBlock(x, y, z, Material.WHITE_CONCRETE)
                }
            }
        }
    }
}