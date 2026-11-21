package dev.thebjoredcraft.creative.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPhysicsEvent

object BlockUpdateListener : Listener {
    @EventHandler
    fun onBlockPhysics(event: BlockPhysicsEvent) {
        event.isCancelled = true
    }
}