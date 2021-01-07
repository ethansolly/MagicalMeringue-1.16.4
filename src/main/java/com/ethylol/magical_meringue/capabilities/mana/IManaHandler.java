package com.ethylol.magical_meringue.capabilities.mana;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IManaHandler extends INBTSerializable<INBT> {

    int MAX_TIER = 10;

    void addMana(int tier, float amt);
    void useMana(int tier, float amt);

    float getMana(int tier);
    void setMana(int tier, float amt);

    CasterState getCasterState();
    void setCasterState(CasterState state);

    int getLvl();
    void setLvl(int lvl);

    enum CasterState {
        DEFAULT,
        ASTRAL,
        PORTAL
    }
}
