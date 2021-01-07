package com.ethylol.magical_meringue.network;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RayTraceMessageHandler implements BiConsumer<RayTraceMessage, Supplier<NetworkEvent.Context>> {

    @Override
    public void accept(RayTraceMessage message, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity playerMP = ctx.get().getSender();
        LazyOptional<IManaHandler> manaHandlerLO = playerMP.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
        manaHandlerLO.ifPresent(manaHandler -> {
            World w = playerMP.world;
            BlockPos pos = message.getPos();

            if (message.getMessageType() == RayTraceMessage.MessageType.BREAK_BLOCK) {

                //Break Block
                BlockState blockState = w.getBlockState(pos);
                if (blockState.getBlockHardness(w, pos) != -1.0f) {
                    float cost = (float) Math.ceil(blockState.getBlockHardness(w, pos) * 30.0f) / 5.0f;
                    if (manaHandler.getMana(0) >= cost) {
                        ctx.get().enqueueWork(() -> {
                            w.destroyBlock(pos, true);
                            manaHandler.useMana(0, cost);
                            Capabilities.sendManaMessageToClient(playerMP, manaHandler);
                        });
                    }
                }


            } else if (message.getMessageType() == RayTraceMessage.MessageType.LEVITATE) {

                //Levitate
                List<Entity> list = w.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)));
                for (Entity e : list) {
                    if (manaHandler.getMana(0) >= 1) {
                        if (e instanceof LivingEntity) {
                            ctx.get().enqueueWork(() -> {
                                ((LivingEntity) e).addPotionEffect(new EffectInstance(Effects.LEVITATION, 200));
                                manaHandler.useMana(0, 1);
                                Capabilities.sendManaMessageToClient(playerMP, manaHandler);
                            });
                        }
                    }
                    else break;
                }
            } else if (message.getMessageType() == RayTraceMessage.MessageType.EXCHANGE) {

                if (!playerMP.getHeldItemOffhand().isEmpty()) {
                    //Exchange
                    List<Entity> list = w.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)));
                    Entity e = list.get(0);
                    if (manaHandler.getMana(1) >= 4) {
                        if (e instanceof LivingEntity) {
                            LivingEntity le = (LivingEntity) e;
                            if (!le.getHeldItemMainhand().isEmpty()) {

                                ItemStack i = playerMP.getHeldItemOffhand().copy();
                                ItemStack j = le.getHeldItemMainhand().copy();
                                le.setHeldItem(Hand.MAIN_HAND, i);
                                playerMP.setHeldItem(Hand.OFF_HAND, j);

                                manaHandler.useMana(1, 4);
                                Capabilities.sendManaMessageToClient(playerMP, manaHandler);

                            } else if (!le.getHeldItemOffhand().isEmpty()) {

                                ItemStack i = playerMP.getHeldItemOffhand().copy();
                                ItemStack j = le.getHeldItemOffhand().copy();
                                le.setHeldItem(Hand.OFF_HAND, i);
                                playerMP.setHeldItem(Hand.OFF_HAND, j);

                                manaHandler.useMana(1, 4);
                                Capabilities.sendManaMessageToClient(playerMP, manaHandler);

                            }
                        }
                    }
                }
            }
        });
    }

}
