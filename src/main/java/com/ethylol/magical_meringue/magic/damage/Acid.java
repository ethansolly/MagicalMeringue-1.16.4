package com.ethylol.magical_meringue.magic.damage;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;

public class Acid extends DamageSource {

    private Entity from;

    public Acid(Entity from) {
        super("Acid");
        this.from = from;
    }

    @Nullable
    @Override
    public Entity getTrueSource() {
        return from;
    }

    @Override
    public boolean isMagicDamage() {
        return true;
    }
}
