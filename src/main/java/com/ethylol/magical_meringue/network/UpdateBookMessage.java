package com.ethylol.magical_meringue.network;

import com.ethylol.magical_meringue.item.ModItems;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class UpdateBookMessage {

    private ItemStack bookStack;


    public UpdateBookMessage() {
        bookStack = new ItemStack(ModItems.spellbook);
    }

    public UpdateBookMessage(ItemStack bookStack) {
        this.bookStack = bookStack;
    }

    public void fromBytes(ByteBuf buf) {

    }

    public ItemStack getBookStack() {
        return bookStack;
    }

    public static class UpdateBookMessageHandler implements BiConsumer<UpdateBookMessage, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(UpdateBookMessage message, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayerEntity player = ctx.get().getSender();
            ItemStack clientBook = message.bookStack;
            ItemStack serverBook = player.getHeldItemMainhand();
            if (serverBook.getItem() == ModItems.spellbook && clientBook.getItem() == ModItems.spellbook)
                ctx.get().enqueueWork(() -> serverBook.setTag(clientBook.getTag()));
        }

    }
}
