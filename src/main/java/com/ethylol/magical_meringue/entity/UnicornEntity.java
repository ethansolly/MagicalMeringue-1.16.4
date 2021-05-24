package com.ethylol.magical_meringue.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class UnicornEntity extends AbstractHorseEntity {

    public UnicornEntity(EntityType<? extends UnicornEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public boolean isTame() { return false; }

    public void setHorseTamed(boolean tamed) {}

    public void setTemper(int temperIn) {}

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 100.0d)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3375d)
                .createMutableAttribute(Attributes.HORSE_JUMP_STRENGTH, 1.0d);
    }

}
