package dev.thebjoredcraft.creative.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandSendEvent

object TabSuggestionListener : Listener {
    @EventHandler
    fun onTabComplete(event: PlayerCommandSendEvent) {
        event.commands.removeAll { it.contains(":") }
    }
}