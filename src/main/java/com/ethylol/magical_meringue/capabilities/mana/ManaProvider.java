package com.ethylol.magical_meringue.capabilities.mana;

import com.ethylol.magical_meringue.capabilities.Capabilities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ManaProvider implements ICapabilitySerializable<CompoundNBT> {

    private final LazyOptional<IManaHandler> cap = LazyOptional.of(ManaHandler::new);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (capability == Capabilities.MANA_HANDLER_CAPABILITY)
            return cap.cast();
        else return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        cap.ifPresent(manaHandler -> {
            compoundNBT.putInt("lvl", manaHandler.getLvl());
            for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
                compoundNBT.putFloat("mana_" + i, manaHandler.getMana(i));
            }
        });
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        cap.ifPresent(manaHandler -> {
            manaHandler.setLvl(nbt.getInt("lvl"));
            for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
                manaHandler.setMana(i, nbt.getFloat("mana_" + i));
            }
        });
    }

}
