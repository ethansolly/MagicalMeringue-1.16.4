package com.ethylol.magical_meringue.magic.effects.one;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;

public class CureMinorWounds implements ISpellEffect {
    @Override
    public void onCast(PlayerEntity caster, World world, BlockPos pos) {
        if (!world.isRemote) {
            LazyOptional<IManaHandler> manaHandlerLO = caster.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            manaHandlerLO.ifPresent(manaHandler -> {
                if (manaHandler.getMana(0) >= 1) {
                    caster.heal(1);
                    manaHandler.useMana(0, 1);
                    Capabilities.sendManaMessageToClient(caster, manaHandler);
                }
            });
        }
    }

    @Override
    public String name() {
        return "Cure Minor Wounds";
    }

    @Override
    public int tier() {
        return 0;
    }
}
