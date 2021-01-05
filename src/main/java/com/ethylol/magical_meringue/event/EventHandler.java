package com.ethylol.magical_meringue.event;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.*;
import com.ethylol.magical_meringue.capabilities.join.IJoinHandler;
import com.ethylol.magical_meringue.capabilities.join.JoinHandler;
import com.ethylol.magical_meringue.capabilities.join.JoinMessage;
import com.ethylol.magical_meringue.capabilities.join.JoinProvider;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.capabilities.mana.ManaProvider;
import com.ethylol.magical_meringue.item.ModItems;
import com.ethylol.magical_meringue.item.Spellbook;
import com.ethylol.magical_meringue.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber()
public class EventHandler {

    //Capabilities
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(MagicalMeringueCore.MODID, "mana"), new ManaProvider());
            event.addCapability(new ResourceLocation(MagicalMeringueCore.MODID, "join"), new JoinProvider());
        }
    }


    //Other Events

    @SubscribeEvent
    public static void playerJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity && !event.getWorld().isRemote) {
            PlayerEntity player = (PlayerEntity) event.getEntity();

            //Add spellbook
            LazyOptional<IJoinHandler> joinHandlerLO = player.getCapability(Capabilities.JOIN_HANDLER_CAPABILITY, null);
            joinHandlerLO.ifPresent(joinHandler -> {
                if (!joinHandler.hasJoined()) {
                    ItemStack stack = new ItemStack(ModItems.spellbook);
                    ItemEntity entityItem = new ItemEntity(event.getWorld(), player.getPosX(), player.getPosY(), player.getPosZ(), stack);
                    entityItem.setNoPickupDelay();
                    event.getWorld().addEntity(entityItem);

                    joinHandler.setJoined(true);
                    MagicalMeringueCore.network.sendTo(new JoinMessage(joinHandler), ((ServerPlayerEntity) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
                }
            });

            LazyOptional<IManaHandler> manaHandlerLO = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            manaHandlerLO.ifPresent(manaHandler -> MagicalMeringueCore.network.sendTo(new ManaMessage(manaHandler), ((ServerPlayerEntity) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT));

        }
    }

    /*
    @SubscribeEvent
    public static void onItemPickup(net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent event) {
        ItemStack stack = event.getStack();
        EntityPlayer player = event.player;
        if (stack.getItem() == ModItems.spellbook) {
            NBTTagCompound compound;
            if (stack.hasTagCompound() && !stack.getTagCompound().hasKey("boundTo")) {
                //MagicalMeringueCore.getLogger().debug("----------Has compound----------");
                compound = stack.getTagCompound();
                compound.setUniqueId("boundTo", player.getUniqueID());
                stack.setTagCompound(compound);
            }
            else if (!stack.hasTagCompound()) {
                //MagicalMeringueCore.getLogger().debug("----------Has NO compound----------");
                compound = new NBTTagCompound();
                compound.setUniqueId("boundTo", player.getUniqueID());
                stack.setTagCompound(compound);
            }
            else {
                //MagicalMeringueCore.getLogger().debug("----------Has compound and is bound???----------");
            }
        }
        else {
            //MagicalMeringueCore.getLogger().debug("----------ISN'T A SPELLBOOK???----------");
        }
    }
     */

    @SubscribeEvent
    public static void cloneEvent(PlayerEvent.Clone event) {
        /*
        NBTBase mana = event.getOriginal().getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null).serializeNBT();
        event.getEntityPlayer().getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null).deserializeNBT(mana);
         */
        if (event.isWasDeath()) {
            LazyOptional<IJoinHandler> joinLO = event.getOriginal().getCapability(Capabilities.JOIN_HANDLER_CAPABILITY, null);
            joinLO.ifPresent(join -> event.getPlayer().getCapability(Capabilities.JOIN_HANDLER_CAPABILITY, null).orElse(new JoinHandler()).setJoined(join.hasJoined()));
        }

    }

    @SubscribeEvent
    public static void playerWakeUp(PlayerWakeUpEvent event) {
        PlayerEntity player = event.getPlayer();
        LazyOptional<IManaHandler> manaHandlerLO = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
        manaHandlerLO.ifPresent(manaHandler -> {
            int level = manaHandler.getLvl();
            for(int i = 0; i < manaHandler.getLvl(); i++) {
                manaHandler.setMana(i, Utils.maxMana(i, level));
            }
        });
    }
}
