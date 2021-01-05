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

public class Wand extends Item {
    public Wand() {
        super(new Properties().maxStackSize(1).group(CreativeTab.TAB));
        setRegistryName(new ResourceLocation(MagicalMeringueCore.MODID, "wand"));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundNBT nbt = stack.getTag();
            String spellName = nbt.getString("spell");
            if (!StringUtils.isNullOrEmpty(spellName)) {
                return true;
            }
        }
        return super.hasEffect(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
        ItemStack stack = player.getHeldItemMainhand();

        if (stack.hasTag() && stack.getTag().contains("spell")) {
            String name = stack.getTag().getString("spell");
            Spellbook.getSpellFromString(name).getEffect().onCast(player, worldIn, new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ()));
            return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(handIn));
        }

        return new ActionResult<>(ActionResultType.FAIL, player.getHeldItem(handIn));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
        if (itemStack.hasTag()) {
            CompoundNBT nbt = itemStack.getTag();
            String spellName = nbt.getString("spell");
            if (!StringUtils.isNullOrEmpty(spellName)) {
                tooltip.add(new StringTextComponent("Current spell: " + spellName));
            }
        }
    }

}
