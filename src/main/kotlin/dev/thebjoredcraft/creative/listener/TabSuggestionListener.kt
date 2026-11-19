package dev.thebjoredcraft.creative.listener

import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendSuggestionsEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandSendEvent
import org.bukkit.event.server.TabCompleteEvent

object TabSuggestionListener : Listener {
    @EventHandler
    fun onTabComplete(event: PlayerCommandSendEvent) {
        event.commands.removeAll { it.contains(":") }
    }
}