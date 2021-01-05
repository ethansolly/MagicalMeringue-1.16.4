package com.ethylol.magical_meringue.capabilities;

import com.ethylol.magical_meringue.capabilities.join.IJoinHandler;
import com.ethylol.magical_meringue.capabilities.join.JoinHandler;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaHandler;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {

    @CapabilityInject(IManaHandler.class)
    public static Capability<IManaHandler> MANA_HANDLER_CAPABILITY = null;

    @CapabilityInject(IJoinHandler.class)
    public static Capability<IJoinHandler> JOIN_HANDLER_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IManaHandler.class, new Storage<>(), ManaHandler::new);
        CapabilityManager.INSTANCE.register(IJoinHandler.class, new Storage<>(), JoinHandler::new);
    }
}
