package com.ethylol.magical_meringue.tileentity;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(MagicalMeringueCore.MODID)
public class ModTileEntityTypes {

    @ObjectHolder("spinning_wheel")
    public static TileEntityType<SpinningWheelTileEntity> spinning_wheel = null;

}
