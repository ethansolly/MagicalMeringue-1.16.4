package com.ethylol.magical_meringue.capabilities.mana;

import com.ethylol.magical_meringue.capabilities.Capabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ManaMessageHandler implements BiConsumer<ManaMessage, Supplier<NetworkEvent.Context>> {

    public void accept(ManaMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = Minecraft.getInstance().player;
            LazyOptional<IManaHandler> manaHandlerLO = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            manaHandlerLO.ifPresent(manaHandler -> {
                manaHandler.setLvl(message.lvl);
                for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
                    manaHandler.setMana(i, message.mana[i]);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
