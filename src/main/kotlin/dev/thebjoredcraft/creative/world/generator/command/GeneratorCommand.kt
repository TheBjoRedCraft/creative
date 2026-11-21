package dev.thebjoredcraft.creative.world.generator.command

import com.mojang.brigadier.arguments.StringArgumentType
import dev.thebjoredcraft.creative.plugin
import dev.thebjoredcraft.creative.world.generator.GeneratorRegistry
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
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
                        Commands.argument("worldname", StringArgumentType.word())
                            .then(
                                Commands.argument("generator", StringArgumentType.word())
                                    .suggests { _, builder ->
                                        val generators = GeneratorRegistry.allNames()
                                        generators.forEach {
                                            builder.suggest(it)
                                        }

                                        builder.buildFuture()
                                    }.executes {
                                        val worldName =
                                            it.getArgument("worldname", String::class.java)
                                        val generatorName =
                                            it.getArgument("generator", String::class.java)

                                        val generator = GeneratorRegistry.byName(generatorName)

                                        if (generator == null) {
                                            it.source.sender.sendRichMessage("<red>Generator '$generatorName' not found.")
                                            return@executes 1
                                        }

                                        applyGenerator(worldName, generator)

                                        it.source.sender.sendRichMessage("<green>Set world generator for world '${worldName}' to '$generatorName'.")
                                        1
                                    })
                    )
                    .build()
            )
        }


    private fun applyGenerator(worldName: String, generator: ChunkGenerator) {
        val worldContainer = plugin.server.worldContainer
        val bukkitYmlFile = worldContainer.parentFile?.let { File(it, "bukkit.yml") }
            ?: File(worldContainer.absolutePath, "../bukkit.yml").canonicalFile

        if (!bukkitYmlFile.exists()) {
            bukkitYmlFile.createNewFile()
        }

        val config = YamlConfiguration.loadConfiguration(bukkitYmlFile)

        config.set(
            "worlds.${worldName}.generator",
            "${plugin.name}:${generator.javaClass.simpleName}"
        )
        config.save(bukkitYmlFile)
    }
}