package com.ethylol.magical_meringue.item;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.client.gui.CreativeTab;
import com.ethylol.magical_meringue.client.gui.GuiSpellbook;
import com.ethylol.magical_meringue.magic.Spell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class Spellbook extends Item {

    public Spellbook() {
        super(new Properties().maxStackSize(1).group(CreativeTab.TAB));
        setRegistryName(new ResourceLocation(MagicalMeringueCore.MODID, "spellbook"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
        ItemStack stack = player.getHeldItemMainhand();

        //Check if book is bound to player
        if (player.isSneaking()) {
            if (!stack.hasTag()) {
                CompoundNBT compound = new CompoundNBT();
                compound.putString("boundTo", player.getName().getString());
                stack.setTag(compound);
            } else {
                CompoundNBT compound = stack.getTag();
                String name = compound.getString("boundTo");
                if (player.getName().getString().equals(name)) {
                    if (worldIn.isRemote) {
                        Minecraft mc = Minecraft.getInstance();
                        mc.displayGuiScreen(new GuiSpellbook(player, stack));
                    }
                }
            }
        }
        else {
            if (stack.hasTag() && stack.getTag().contains("spell")) {
                String name = stack.getTag().getString("spell");
                getSpellFromString(name).getEffect().onCast(player, worldIn, new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ()));
            }
        }

        return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(handIn));
    }

    public static Spell getSpellFromString(String name) {
        List<Spell> spells = Spell.list;
        for (Spell spell : spells) {
            if (spell.getEffect().name().equals(name)) {
                return spell;
            }
        }
        return null;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return itemStack.copy();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
        if (itemStack.hasTag()) {
            CompoundNBT nbt = itemStack.getTag();
            String s = nbt.getString("boundTo");
            if (!StringUtils.isNullOrEmpty(s)) {
               //MagicalMeringueCore.getLogger().debug("------------BOUND TO???----------------");
                tooltip.add(new StringTextComponent("Bound to " + s));
            }

            String spellName = nbt.getString("spell");
            if (!StringUtils.isNullOrEmpty(spellName)) {
                //MagicalMeringueCore.getLogger().debug("------------BOUND TO???----------------");
                tooltip.add(new StringTextComponent("Current spell: " + spellName));
            }
        }
    }
}
