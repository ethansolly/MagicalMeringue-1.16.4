package com.ethylol.magical_meringue.capabilities.mana;

import io.netty.buffer.ByteBuf;

public class ManaMessage {

    int lvl;
    float[] mana;
    IManaHandler.CasterState state;

    public ManaMessage() {
        lvl = 1;
        mana = new float[IManaHandler.MAX_TIER];
        state = IManaHandler.CasterState.DEFAULT;
    }

    public ManaMessage(IManaHandler manaHandler) {
        lvl = manaHandler.getLvl();
        mana = new float[IManaHandler.MAX_TIER];
        for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
            mana[i] = manaHandler.getMana(i);
        }
        state = manaHandler.getCasterState();
    }

    public ManaMessage(int lvl, float...mana) {
        this.lvl = lvl;
        this.mana = mana;
        state = IManaHandler.CasterState.DEFAULT;
    }

    public int getLvl() {
        return lvl;
    }

    public float[] getMana() {
        return mana;
    }

    public IManaHandler.CasterState getState() { return state; }
}
