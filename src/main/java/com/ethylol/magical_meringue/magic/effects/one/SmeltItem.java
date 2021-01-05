package com.ethylol.magical_meringue.magic.effects.one;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.ArrayList;
import java.util.Optional;

public class SmeltItem implements ISpellEffect {

    @Override
    public void onCast(PlayerEntity caster, World world, BlockPos pos) {
        if (!world.isRemote) {
            LazyOptional<IManaHandler> manaHandlerLO = caster.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            manaHandlerLO.ifPresent(manaHandler -> {
                if (manaHandler.getMana(0) >= 4) {
                    ArrayList<Integer> list = new ArrayList<>();
                    for (int i = 0; i < caster.inventory.getSizeInventory(); i++) {
                        ItemStack stack = caster.inventory.getStackInSlot(i);
                        Optional<FurnaceRecipe> optional = world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(stack), world);
                        if (optional.isPresent()) {
                            ItemStack itemStack = optional.get().getRecipeOutput();
                            if (!itemStack.isEmpty())
                                list.add(i);
                        }
                    }
                    if (!list.isEmpty()) {
                        int randIndex = list.get((int) (Math.random() * list.size()));
                        ItemStack smeltMe = caster.inventory.decrStackSize(randIndex, 1);
                        Optional<FurnaceRecipe> optional = world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(smeltMe), world);
                        if (optional.isPresent()) {
                            caster.inventory.addItemStackToInventory(optional.get().getRecipeOutput().copy());
                            manaHandler.useMana(0, 4);
                            MagicalMeringueCore.network.sendTo(new ManaMessage(manaHandler), ((ServerPlayerEntity)caster).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
                        }
                    }
                }
            });
        }
    }

    @Override
    public String name() {
        return "Smelt Item";
    }

    @Override
    public int tier() {
        return 0;
    }
}
