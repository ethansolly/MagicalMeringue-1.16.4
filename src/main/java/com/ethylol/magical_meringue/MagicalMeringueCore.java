package com.ethylol.magical_meringue;

import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.entity.CaveLordEntity;
import com.ethylol.magical_meringue.entity.ModEntities;
import com.ethylol.magical_meringue.entity.UnicornEntity;
import com.ethylol.magical_meringue.event.RegistrationHandler;
import com.ethylol.magical_meringue.network.NetworkHandler;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("magical_meringue")
public class MagicalMeringueCore {

    public static final String MODID = "magical_meringue";
    private static final String PROTOCOL_VERSION = "1";

    //Add config

    public static SimpleChannel network;

    private static Logger logger = LogManager.getLogger();

    public MagicalMeringueCore() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, RegistrationHandler::registerRecipe);

    }

    public void setup(final FMLCommonSetupEvent event) {

        network = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(MODID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        NetworkHandler.register(network);
        Capabilities.register();

        event.enqueueWork(() -> {
            GlobalEntityTypeAttributes.put(ModEntities.unicorn, UnicornEntity.setCustomAttributes().create());
            GlobalEntityTypeAttributes.put(ModEntities.cave_lord, CaveLordEntity.setCustomAttributes().create());
        });

    }

    public void clientSetup(final FMLClientSetupEvent event) {
        //Client setup
        RegistrationHandler.registerEntityRenderers();
    }

    public void enqueueIMC(final InterModEnqueueEvent event) {
        //Intermod comms
    }

    public void processIMC(final InterModProcessEvent event) {
        //Recieve/process imc
    }
    public static Logger getLogger() {
        return logger;
    }


}
