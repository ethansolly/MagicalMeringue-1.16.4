package com.ethylol.magical_meringue.magic.effects.two;

import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Sleepwalking implements ISpellEffect {
    @Override
    public void onCast(PlayerEntity caster, World world, BlockPos pos) {

    }

    @Override
    public String name() {
        return "Sleepwalking";
    }

    @Override
    public int tier() {
        return 1;
    }
}
