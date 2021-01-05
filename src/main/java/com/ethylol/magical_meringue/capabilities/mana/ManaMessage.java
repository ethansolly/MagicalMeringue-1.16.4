package com.ethylol.magical_meringue.capabilities.mana;

import io.netty.buffer.ByteBuf;

public class ManaMessage {

    int lvl;
    float[] mana;

    public ManaMessage() {
        lvl = 1;
        mana = new float[IManaHandler.MAX_TIER];
    }

    public ManaMessage(IManaHandler manaHandler) {
        lvl = manaHandler.getLvl();
        mana = new float[IManaHandler.MAX_TIER];
        for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
            mana[i] = manaHandler.getMana(i);
        }
    }

    public ManaMessage(int lvl, float...mana) {
        this.lvl = lvl;
        this.mana = mana;
    }

    public int getLvl() {
        return lvl;
    }

    public float[] getMana() {
        return mana;
    }
}
