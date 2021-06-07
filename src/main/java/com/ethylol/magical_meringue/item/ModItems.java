package com.ethylol.magical_meringue.item;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(MagicalMeringueCore.MODID)
public class ModItems {

    //ObjectHolder Entries

    @ObjectHolder("spellbook")
    public static Item spellbook = null;

    @ObjectHolder("wand")
    public static Item wand = null;

    @ObjectHolder("tachium_ingot")
    public static Item tachium_ingot = null;

    @ObjectHolder("platonium_ingot")
    public static Item platonium_ingot = null;

    @ObjectHolder("magentium_strand")
    public static Item magentium_strand = null;

}
