package dev.thebjoredcraft.creative.listener

import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper
import com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerPluginMessage
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPluginMessage
import com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerResponse
import com.google.gson.JsonObject
import dev.thebjoredcraft.creative.COLOR
import dev.thebjoredcraft.creative.miniMessage
import dev.thebjoredcraft.creative.plugin
import io.github.retrooper.packetevents.adventure.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit

object ServerPingPacketListener : PacketListenerAbstract() {
    override fun onPacketSend(event: PacketSendEvent) {
        when (event.packetType) {
            PacketType.Configuration.Server.PLUGIN_MESSAGE -> {
                val packet = WrapperConfigServerPluginMessage(event)
                if (packet.getChannelName() != "minecraft:brand") {
                    return
                }

                packet.setData(brandBytes)
                event.markForReEncode(true)
            }

            PacketType.Play.Server.PLUGIN_MESSAGE -> {
                val packet = WrapperPlayServerPluginMessage(event)
                if (packet.getChannelName() != "minecraft:brand") {
                    return
                }

                packet.setData(brandBytes)
                event.markForReEncode(true)
            }

            PacketType.Status.Server.RESPONSE -> {
                val packet = WrapperStatusServerResponse(event)
                val component = packet.component

                val version = component.get("version")?.takeIf { it.isJsonObject }?.asJsonObject
                    ?: JsonObject()
                val players = component.get("players")?.takeIf { it.isJsonObject }?.asJsonObject
                    ?: JsonObject()

                version.addProperty("name", brandString)

                players.addProperty("max", 500)

                component.add("version", version)
                component.add("players", players)
                component.add(
                    "description",
                    GsonComponentSerializer.gson().serializeToTree(motdComponent)
                )

                packet.component = component
                event.markForReEncode(true)
            }

            else -> return
        }
    }

    private val brandBytes by lazy {
        val buffer = UnpooledByteBufAllocationHelper.buffer()

        try {
            val wrapper = PacketWrapper.createUniversalPacketWrapper(buffer)
            wrapper.writeString(brandString)

            ByteArray(ByteBufHelper.readableBytes(buffer)).also {
                ByteBufHelper.readBytes(buffer, it)
            }
        } finally {
            ByteBufHelper.release(buffer)
        }
    }

    private val brandString by lazy {
        LegacyComponentSerializer.legacySection().serialize(
            miniMessage.deserialize(
                "${COLOR}Creative v${plugin.description.version} @MCv${Bukkit.getMinecraftVersion()}<reset>"
            )
        )
    }

    private val motdComponent by lazy {
        miniMessage.deserialize(
            "                   <red>ᴀ ᴘʀɪᴠᴀᴛᴇ ѕᴇʀᴠᴇʀ\n" +
                    "             <white>Running creative v${plugin.description.version} - [${COLOR}${Bukkit.getMinecraftVersion()}<white>]"
        )
    }
}