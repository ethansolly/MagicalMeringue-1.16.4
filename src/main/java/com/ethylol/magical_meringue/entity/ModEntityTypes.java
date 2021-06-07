package com.ethylol.magical_meringue.entity;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(MagicalMeringueCore.MODID)
public class ModEntityTypes {

    @ObjectHolder("unicorn")
    public static EntityType<UnicornEntity> unicorn = null;

    @ObjectHolder("cave_lord")
    public static EntityType<CaveLordEntity> cave_lord = null;

}
