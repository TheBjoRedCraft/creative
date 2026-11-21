package dev.thebjoredcraft.creative

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.thebjoredcraft.creative.command.ClearCommand
import dev.thebjoredcraft.creative.command.GameModeCommand
import dev.thebjoredcraft.creative.listener.AsyncChatListener
import dev.thebjoredcraft.creative.listener.BlockUpdateListener
import dev.thebjoredcraft.creative.listener.TabSuggestionListener
import dev.thebjoredcraft.creative.listener.WhitelistListener
import dev.thebjoredcraft.creative.warps.WarpCommand
import dev.thebjoredcraft.creative.warps.config.WarpConfigManager
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(PluginMain::class.java)

val TEXT_COLOR = TextColor.fromHexString("#6eff94")
val COLOR = "<${TEXT_COLOR?.asHexString()}>"
val PREFIX = "<dark_gray>»$COLOR Creative <dark_gray>|<white>"
val miniMessage = MiniMessage.miniMessage()

class PluginMain : SuspendingJavaPlugin() {
    override suspend fun onEnableAsync() {
        GameModeCommand.register()
        ClearCommand.register()
        WarpCommand.register()

        Bukkit.getPluginManager().registerEvents(BlockUpdateListener, this)
        Bukkit.getPluginManager().registerEvents(TabSuggestionListener, this)
        Bukkit.getPluginManager().registerEvents(WhitelistListener, this)
        Bukkit.getPluginManager().registerEvents(AsyncChatListener, this)

        WarpConfigManager.loadAll()
    }

    override suspend fun onDisableAsync() {
        WarpConfigManager.saveAll()
    }
}