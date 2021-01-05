package com.ethylol.magical_meringue.magic.effects.one;

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

public class InvisibilityFlash implements ISpellEffect {
    @Override
    public void onCast(PlayerEntity caster, World world, BlockPos pos) {
        if (!world.isRemote) {
            LazyOptional<IManaHandler> manaHandlerLO = caster.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            manaHandlerLO.ifPresent(manaHandler -> {
                if (manaHandler.getMana(0) >= 8) {
                    caster.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 200));
                    manaHandler.useMana(0, 8);
                    MagicalMeringueCore.network.sendTo(new ManaMessage(manaHandler), ((ServerPlayerEntity) caster).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
                }
            });
        }
    }

    @Override
    public String name() {
        return "Flash of Invisibility";
    }

    @Override
    public int tier() {
        return 0;
    }
}
