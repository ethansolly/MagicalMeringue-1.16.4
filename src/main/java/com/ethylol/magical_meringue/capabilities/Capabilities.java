package com.ethylol.magical_meringue.capabilities;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.join.IJoinHandler;
import com.ethylol.magical_meringue.capabilities.join.JoinHandler;
import com.ethylol.magical_meringue.capabilities.join.JoinMessage;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.network.NetworkDirection;

public class Capabilities {

    @CapabilityInject(IManaHandler.class)
    public static Capability<IManaHandler> MANA_HANDLER_CAPABILITY = null;

    @CapabilityInject(IJoinHandler.class)
    public static Capability<IJoinHandler> JOIN_HANDLER_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IManaHandler.class, new Storage<>(), ManaHandler::new);
        CapabilityManager.INSTANCE.register(IJoinHandler.class, new Storage<>(), JoinHandler::new);
    }

    public static void sendManaMessageToClient(PlayerEntity playerEntity, IManaHandler manaHandler) {
        MagicalMeringueCore.network.sendTo(new ManaMessage(manaHandler), ((ServerPlayerEntity) playerEntity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendJoinMessageToClient(PlayerEntity playerEntity, IJoinHandler joinHandler) {
        MagicalMeringueCore.network.sendTo(new JoinMessage(joinHandler), ((ServerPlayerEntity) playerEntity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }
}
