package com.ethylol.magical_meringue.client.gui;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTab extends ItemGroup {

    public static final ItemGroup TAB = new CreativeTab();

    public CreativeTab() {
        super(MagicalMeringueCore.MODID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.spellbook);
    }
}
