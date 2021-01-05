package com.ethylol.magical_meringue.capabilities.join;

import com.ethylol.magical_meringue.capabilities.Capabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class JoinMessageHandler implements BiConsumer<JoinMessage, Supplier<NetworkEvent.Context>> {

    public void accept(JoinMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = Minecraft.getInstance().player;
            LazyOptional<IJoinHandler> manaHandlerLO = player.getCapability(Capabilities.JOIN_HANDLER_CAPABILITY, null);
            manaHandlerLO.ifPresent(manaHandler -> manaHandler.setJoined(message.joined));
        });
        ctx.get().setPacketHandled(true);
    }
}
