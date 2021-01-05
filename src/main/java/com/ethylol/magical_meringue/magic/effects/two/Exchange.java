package com.ethylol.magical_meringue.magic.effects.two;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import com.ethylol.magical_meringue.network.RayTraceMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class Exchange implements ISpellEffect {

    @Override
    public void onCast(PlayerEntity caster, World world, BlockPos pos) {
        if (world.isRemote) {
            RayTraceResult lookingAt = Minecraft.getInstance().objectMouseOver;
            if (lookingAt != null && lookingAt.getType() == RayTraceResult.Type.ENTITY) {
                MagicalMeringueCore.network.sendToServer(new RayTraceMessage(lookingAt, RayTraceMessage.MessageType.EXCHANGE));
            }
        }
    }

    @Override
    public String name() {
        return "Exchange";
    }

    @Override
    public int tier() {
        return 1;
    }
}
