package com.ethylol.magical_meringue.magic.effects.one;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import com.ethylol.magical_meringue.network.RayTraceMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class AcidSplash implements ISpellEffect {
    @Override
    public void onCast(PlayerEntity caster, World world, BlockPos pos) {
        if (world.isRemote) {
            RayTraceResult lookingAt = Minecraft.getInstance().objectMouseOver;
            if (lookingAt != null && lookingAt.getType() == RayTraceResult.Type.ENTITY) {
                Vector3d hitVec = lookingAt.getHitVec();
                MagicalMeringueCore.network.sendToServer(new RayTraceMessage(lookingAt, RayTraceMessage.MessageType.ACID_SPLASH));

                //add particle effects
                for(int j = 0; j < 8; ++j) {
                    float f = world.rand.nextFloat() * ((float)Math.PI * 2F);
                    float f1 = world.rand.nextFloat() * 0.5F + 0.5F;
                    float f2 = MathHelper.sin(f) * 3 * f1;
                    float f3 = MathHelper.cos(f) * 3 * f1;
                    world.addParticle(ParticleTypes.ITEM_SLIME, hitVec.getX() + (double)f2, hitVec.getY(), hitVec.getZ() + (double)f3, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public String name() {
        return "Acid Splash";
    }

    @Override
    public int tier() {
        return 0;
    }
}
