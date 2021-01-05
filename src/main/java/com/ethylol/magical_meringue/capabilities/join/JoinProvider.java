package com.ethylol.magical_meringue.capabilities.join;

import com.ethylol.magical_meringue.capabilities.Capabilities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JoinProvider implements ICapabilitySerializable<CompoundNBT> {

    private final LazyOptional<IJoinHandler> cap = LazyOptional.of(JoinHandler::new);

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == Capabilities.JOIN_HANDLER_CAPABILITY)
            return cap.cast();
        else return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        if (cap.isPresent()) {
            IJoinHandler joinHandler = cap.orElse(new JoinHandler());
            compound.putBoolean("joined", joinHandler.hasJoined());
        }
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        IJoinHandler joinHandler = cap.orElse(new JoinHandler());
        joinHandler.setJoined(nbt.getBoolean("joined"));
    }
}
