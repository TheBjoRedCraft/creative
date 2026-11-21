package dev.thebjoredcraft.creative.world.generator.command

import com.mojang.brigadier.arguments.StringArgumentType
import dev.thebjoredcraft.creative.plugin
import dev.thebjoredcraft.creative.world.generator.GeneratorRegistry
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.generator.ChunkGenerator
import java.io.File

object GeneratorCommand {
    fun register() =
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(
                Commands.literal("setworldgenerator")
                    .requires { it.sender.hasPermission("creative.setworldgenerator") }
                    .then(
                        Commands.argument("world", ArgumentTypes.world())
                            .then(
                                Commands.argument("generator", StringArgumentType.word())
                                    .suggests { _, builder ->
                                        val generators = GeneratorRegistry.allNames()
                                        generators.forEach {
                                            builder.suggest(it)
                                        }

                                        builder.buildFuture()
                                    }.executes {
                                        val world =
                                            it.getArgument("world", World::class.java)
                                        val generatorName =
                                            it.getArgument("generator", String::class.java)

                                        val generator = GeneratorRegistry.byName(generatorName)

                                        if (generator == null) {
                                            it.source.sender.sendRichMessage("<red>Generator '$generatorName' not found.")
                                            return@executes 1
                                        }

                                        applyGenerator(world, generator)

                                        it.source.sender.sendRichMessage("<green>Set world generator for world '${world.name}' to '$generatorName'.")
                                        1
                                    })
                    )
                    .build()
            )
        }


    private fun applyGenerator(world: World, generator: ChunkGenerator) {
        val worldContainer = plugin.server.worldContainer
        val bukkitYmlFile = worldContainer.parentFile?.let { File(it, "bukkit.yml") }
            ?: File(worldContainer.absolutePath, "../bukkit.yml").canonicalFile

        if (!bukkitYmlFile.exists()) {
            bukkitYmlFile.createNewFile()
        }

        val config = YamlConfiguration.loadConfiguration(bukkitYmlFile)

        config.set(
            "worlds.${world.name}.generator",
            "${plugin.name}:${generator.javaClass.simpleName}"
        )
        config.save(bukkitYmlFile)
    }
}