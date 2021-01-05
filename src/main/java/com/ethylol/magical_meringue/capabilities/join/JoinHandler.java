package com.ethylol.magical_meringue.capabilities.join;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class JoinHandler implements IJoinHandler {

    boolean joined;

    @Override
    public boolean hasJoined() {
        return joined;
    }

    @Override
    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putBoolean("joined", joined);
        return compound;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        joined = ((CompoundNBT) nbt).getBoolean("joined");
    }
}
