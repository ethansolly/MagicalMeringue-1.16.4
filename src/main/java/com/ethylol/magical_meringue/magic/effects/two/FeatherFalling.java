package com.ethylol.magical_meringue.magic.effects.two;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;

public class FeatherFalling implements ISpellEffect {
    @Override
    public void onCast(PlayerEntity caster, World world, BlockPos pos) {
        if (!world.isRemote) {
            LazyOptional<IManaHandler> manaHandlerLO = caster.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            manaHandlerLO.ifPresent(iManaHandler -> {
                if (iManaHandler.getMana(1) >= 8) {
                    caster.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 200));
                    iManaHandler.useMana(1, 8);
                    MagicalMeringueCore.network.sendTo(new ManaMessage(iManaHandler), ((ServerPlayerEntity) caster).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
                }
            });
        }
    }

    @Override
    public String name() {
        return "Feather Falling";
    }

    @Override
    public int tier() {
        return 1;
    }
}
