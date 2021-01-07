package com.ethylol.magical_meringue.network;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.join.JoinMessage;
import com.ethylol.magical_meringue.capabilities.join.JoinMessageHandler;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessageHandler;
import com.ethylol.magical_meringue.item.ModItems;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.io.IOException;

public class NetworkHandler {

    private static int discriminator = 0;

    public static void register(SimpleChannel network) {
        network.registerMessage(discriminator++, ManaMessage.class, (manaMessage, buffer) -> {
            buffer.writeInt(manaMessage.getLvl());
            for (float f : manaMessage.getMana()) {
                buffer.writeFloat(f);
            }
        }, buffer -> {
            int lvl = buffer.readInt();
            float[] mana = new float[IManaHandler.MAX_TIER];
            for (int i = 0; i < mana.length; i++) {
                mana[i] = buffer.readFloat();
            }
            return new ManaMessage(lvl, mana);
        }, new ManaMessageHandler());


        network.registerMessage(discriminator++, JoinMessage.class, (joinMessage, buffer) -> buffer.writeBoolean(joinMessage.isJoined()), buffer -> new JoinMessage(buffer.readBoolean()), new JoinMessageHandler());


        network.registerMessage(discriminator++, RayTraceMessage.class, (message, buf) -> {
            buf.writeInt(message.getPos().getX());
            buf.writeInt(message.getPos().getY());
            buf.writeInt(message.getPos().getZ());
            buf.writeInt(message.getHitType().ordinal());
            if (message.getHitType() == RayTraceResult.Type.BLOCK) {
                buf.writeInt(message.getFacing().ordinal());
            }
            buf.writeInt(message.getMessageType().ordinal());
        }, buf -> {
            BlockPos pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            RayTraceResult.Type hitType = RayTraceResult.Type.values()[buf.readInt()];
            Direction facing = null;
            if (hitType == RayTraceResult.Type.BLOCK)
                facing = Direction.values()[buf.readInt()];
            RayTraceMessage.MessageType messageType = RayTraceMessage.MessageType.values()[buf.readInt()];
            return new RayTraceMessage(hitType, pos, facing, messageType);
        }, new RayTraceMessageHandler());

        network.registerMessage(discriminator++, UpdateBookMessage.class, (updateBookMessage, buf) -> {
            ItemStack bookStack = updateBookMessage.getBookStack();
            if (bookStack.hasTag()) {
                try {
                    CompressedStreamTools.write(bookStack.getTag(), new ByteBufOutputStream(buf));
                } catch (IOException e) {
                    throw new EncoderException(e);
                }
            }
            else {
                buf.writeByte(0);
            }
        }, buf -> {
            ItemStack bookStack = new ItemStack(ModItems.spellbook);
            int i = buf.readerIndex();
            if (buf.readByte() != 0) {
                buf.readerIndex(i); //reset the index
                try {
                    bookStack.setTag(CompressedStreamTools.read(new ByteBufInputStream(buf), new NBTSizeTracker(2097152L)));
                } catch (IOException e) {
                    throw new EncoderException(e);
                }
            }
            else {
                bookStack.setTag(null);
            }
            return new UpdateBookMessage(bookStack);
        }, new UpdateBookMessage.UpdateBookMessageHandler());
    }

}
