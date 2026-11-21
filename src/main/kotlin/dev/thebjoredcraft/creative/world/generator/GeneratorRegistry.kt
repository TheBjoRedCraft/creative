package dev.thebjoredcraft.creative.world.generator

import dev.thebjoredcraft.creative.plugin
import dev.thebjoredcraft.creative.world.generator.impl.GrayWorldGenerator
import dev.thebjoredcraft.creative.world.generator.impl.VoidWorldGenerator
import dev.thebjoredcraft.creative.world.generator.impl.WhiteWorldGenerator
import org.bukkit.generator.ChunkGenerator

object GeneratorRegistry {
    private val generators = mutableMapOf<String, ChunkGenerator>()

    fun byName(name: String): ChunkGenerator? = generators[name]
    fun allNames() = generators.map { it.key }

    fun registerAll() {
        generators["void"] = VoidWorldGenerator
        generators["gray"] = GrayWorldGenerator
        generators["white"] = WhiteWorldGenerator


        plugin.logger.info("Registered ${generators.size} world generators.")
    }
}