package com.ethylol.magical_meringue.capabilities.join;

import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IJoinHandler extends INBTSerializable<INBT> {
    boolean hasJoined();
    void setJoined(boolean joined);
}
