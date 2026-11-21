package dev.thebjoredcraft.creative.listener

import org.bukkit.entity.FallingBlock
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPhysicsEvent
import org.bukkit.event.entity.EntityChangeBlockEvent

object BlockUpdateListener : Listener {
    @EventHandler
    fun onBlockPhysics(event: BlockPhysicsEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onEntityChangeBlock(event: EntityChangeBlockEvent) {
        if (event.entity is FallingBlock) {
            event.isCancelled = true
        }
    }
}