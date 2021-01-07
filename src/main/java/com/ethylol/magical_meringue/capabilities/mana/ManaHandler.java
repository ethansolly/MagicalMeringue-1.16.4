package com.ethylol.magical_meringue.capabilities.mana;

import com.ethylol.magical_meringue.utils.Utils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class ManaHandler implements IManaHandler {

    int lvl;
    float[] mana;
    CasterState casterState;

    public ManaHandler() {
        lvl = 10;
        mana = new float[IManaHandler.MAX_TIER];
        for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
            mana[i] = Utils.maxMana(i, lvl);
        }
        casterState = CasterState.DEFAULT;
    }

    @Override
    public void addMana(int tier, float amt) {
        mana[tier] += amt;
    }

    @Override
    public void useMana(int tier, float amt) {
        mana[tier] -= amt;
    }

    @Override
    public float getMana(int tier) {
        return mana[tier];
    }

    @Override
    public void setMana(int tier, float amt) {
        mana[tier] = amt;
    }

    @Override
    public CasterState getCasterState() {
        return casterState;
    }

    @Override
    public void setCasterState(CasterState state) {
        casterState = state;
    }

    @Override
    public int getLvl() {
        return lvl;
    }

    @Override
    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    //The tag mana_i and its corresponding entry mana[i] correspond to the (i+1)-th tier.

    @Override
    public INBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("lvl", lvl);
        for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
            compound.putFloat("mana_" + i, mana[i]);
        }
        compound.putInt("state", casterState.ordinal());
        return compound;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        lvl = compound.getInt("lvl");
        for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
            mana[i] = compound.getFloat("mana_" + i);
        }
        casterState = CasterState.values()[compound.getInt("state")];
    }
}
