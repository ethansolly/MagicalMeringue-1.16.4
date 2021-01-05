package com.ethylol.magical_meringue.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISpellEffect {
    void onCast(PlayerEntity caster, World world, BlockPos pos);
    String name();
    int tier();
}
