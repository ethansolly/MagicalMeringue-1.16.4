package com.ethylol.magical_meringue.magic.effects.two;

import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class AstralProjection implements ISpellEffect {
    @Override
    public void onCast(PlayerEntity caster, World world, BlockPos pos) {
        if (!world.isRemote) {
            LazyOptional<IManaHandler> lazyOptional = caster.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            lazyOptional.ifPresent(iManaHandler -> {
                if (iManaHandler.getMana(1) > 0) {
                    double mana = iManaHandler.getMana(1);
                    iManaHandler.setMana(1, 0);
                    iManaHandler.setCasterState(IManaHandler.CasterState.ASTRAL);
                    Capabilities.sendManaMessageToClient(caster, iManaHandler);
                }
            });
        }
    }

    @Override
    public String name() {
        return "Astral Projection";
    }

    @Override
    public int tier() {
        return 1;
    }
}
