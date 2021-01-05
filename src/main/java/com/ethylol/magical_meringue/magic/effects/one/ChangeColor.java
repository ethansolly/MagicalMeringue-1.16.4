package com.ethylol.magical_meringue.magic.effects.one;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.List;

public class ChangeColor implements ISpellEffect {
    @Override
    public void onCast(PlayerEntity caster, World world, BlockPos pos) {
        if (world.isRemote) {
            LazyOptional<IManaHandler> manaHandlerLO = caster.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            manaHandlerLO.ifPresent(manaHandler  -> {
                if (manaHandler.getMana(0) >= 2) {
                    for (ItemStack stack : caster.inventory.armorInventory) {
                        if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem) {
                            int random = (int)(Math.random()*16777215);
                            ((IDyeableArmorItem)stack.getItem()).setColor(stack, random);
                        }
                    }
                    manaHandler.useMana(0, 2);
                    MagicalMeringueCore.network.sendToServer(new ManaMessage(manaHandler));
                }

            });
        }
    }

    @Override
    public String name() {
        return "Change Color";
    }

    @Override
    public int tier() {
        return 0;
    }
}
