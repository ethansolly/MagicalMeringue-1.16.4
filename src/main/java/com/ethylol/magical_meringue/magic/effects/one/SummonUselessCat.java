package com.ethylol.magical_meringue.magic.effects.one;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;

public class SummonUselessCat implements ISpellEffect {
    @Override
    public void onCast(PlayerEntity caster, World world, BlockPos pos) {
        if (!world.isRemote) {
            LazyOptional<IManaHandler> manaHandlerLO = caster.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            if (manaHandlerLO.isPresent()) {
                IManaHandler manaHandler = manaHandlerLO.orElse(new ManaHandler());
                if (manaHandler.getMana(0) >= 4) {
                    OcelotEntity ocelot = new OcelotEntity(EntityType.OCELOT, world);
                    ocelot.setPosition(caster.getPosX(), caster.getPosY(), caster.getPosZ());
                    world.addEntity(ocelot);

                    manaHandler.useMana(0, 4);
                    Capabilities.sendManaMessageToClient(caster, manaHandler);
                }
            }
        }
    }

    @Override
    public String name() {
        return "Summon Useless Cat";
    }

    @Override
    public int tier() {
        return 0;
    }
}
