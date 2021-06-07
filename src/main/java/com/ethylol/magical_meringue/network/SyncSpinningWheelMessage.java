package com.ethylol.magical_meringue.network;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class SyncSpinningWheelMessage {

    private BlockPos pos;
    private ItemStack itemStack;

    public SyncSpinningWheelMessage() {
        this.pos = BlockPos.ZERO;
        this.itemStack = ItemStack.EMPTY;
    }

    public SyncSpinningWheelMessage(BlockPos pos, ItemStack itemStack) {
        this.pos = pos;
        this.itemStack = itemStack;
    }

    public BlockPos getPos() {
        return pos;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
