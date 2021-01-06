package com.ethylol.magical_meringue.magic.effects.two;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;
import org.lwjgl.system.CallbackI;

import java.util.Random;

public class Phasing implements ISpellEffect {

    @Override
    public void onCast(PlayerEntity caster, World world, BlockPos pos) {
        if (!world.isRemote) {
            LazyOptional<IManaHandler> manaHandlerLO = caster.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            manaHandlerLO.ifPresent(iManaHandler -> {
                if (iManaHandler.getMana(1) >= 4) {
                    Random rand = new Random();
                    double x = caster.getPosX() + (rand.nextDouble() - 0.5D) * 64.0D;
                    double z = caster.getPosZ() + (rand.nextDouble() - 0.5D) * 64.0D;
                    double y = world.getHeight(Heightmap.Type.WORLD_SURFACE, (int)x, (int)z);
                    if (caster.attemptTeleport(x, y, z,true)) {
                        iManaHandler.useMana(1, 4);
                        MagicalMeringueCore.network.sendTo(new ManaMessage(iManaHandler), ((ServerPlayerEntity) caster).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
                    }
                }
            });
        }
    }

    @Override
    public String name() {
        return "Phasing";
    }

    @Override
    public int tier() {
        return 1;
    }
}
