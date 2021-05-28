package com.ethylol.magical_meringue.magic.effects.two;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import com.ethylol.magical_meringue.network.RayTraceMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.UUID;

public class Lasso implements ISpellEffect {

    private static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("6749fb81-c604-4b70-8c46-7381c7c0742e");

    @Override
    public void onCast(PlayerEntity caster, World world, BlockPos pos) {

        if (world.isRemote) {
            double d0 = 17;
            RayTraceResult result = caster.pick(d0, 1.0F, false);
            Vector3d vector3d = caster.getEyePosition(1.0F);
            Vector3d vector3d1 = caster.getLook(1.0F);
            Vector3d vector3d2 = vector3d.add(vector3d1.x * d0, vector3d1.y * d0, vector3d1.z * d0);
            AxisAlignedBB axisalignedbb = caster.getBoundingBox().expand(vector3d1.scale(d0)).grow(1.0D, 1.0D, 1.0D);
            double d1 = result.getHitVec().squareDistanceTo(vector3d);
            EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(caster, vector3d, vector3d2, axisalignedbb, (p_215312_0_) -> {
                return !p_215312_0_.isSpectator() && p_215312_0_.canBeCollidedWith();
            }, d1);

            if (entityraytraceresult != null) {
                MagicalMeringueCore.network.sendToServer(new RayTraceMessage(entityraytraceresult, RayTraceMessage.MessageType.LASSO));
            }
        }
    }

    @Override
    public String name() {
        return "Lasso";
    }

    @Override
    public int tier() {
        return 1;
    }
}
