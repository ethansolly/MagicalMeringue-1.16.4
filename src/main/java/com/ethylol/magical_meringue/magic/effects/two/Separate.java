package com.ethylol.magical_meringue.magic.effects.two;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.block.ModBlocks;
import com.ethylol.magical_meringue.item.ModItems;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import com.ethylol.magical_meringue.network.RayTraceMessage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.state.properties.Half;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class Separate implements ISpellEffect {

    private static boolean registered = false;
    private static final Map<Block, Function<World, Item>> separationMap = new HashMap<>();

    public static void registerSeparationMap() {
        separationMap.put(ModBlocks.platonium_block, w -> ModItems.platonium_ingot);
        separationMap.put(ModBlocks.magentium_block, w -> ModItems.magentium_strand);
        for (Block b : BlockTags.WOOL.getAllElements()) {
            separationMap.put(b, w -> Items.STRING);
        }
        for (Block b : BlockTags.LOGS.getAllElements()) {
            separationMap.put(b, w ->Items.STICK);
        }
        for (Block b : Tags.Blocks.ORES.getAllElements()) {
            separationMap.put(b, w -> {
                Optional<FurnaceRecipe> optional = w.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(new ItemStack(b)), w);
                return optional.map(furnaceRecipe -> furnaceRecipe.getRecipeOutput().getItem()).orElse(null);
            });
        }

        registered = true;

    }

    public static boolean isRegistered() {
        return registered;
    }

    public static Map<Block, Function<World, Item>> getSeparationMap() {
        return separationMap;
    }

    @Override
    public void onCast(PlayerEntity caster, World world, BlockPos pos) {
        if (world.isRemote) {
            RayTraceResult lookingAt = Minecraft.getInstance().objectMouseOver;
            if (lookingAt != null && lookingAt.getType() == RayTraceResult.Type.BLOCK) {
                MagicalMeringueCore.network.sendToServer(new RayTraceMessage(lookingAt, RayTraceMessage.MessageType.SEPARATE));
            }
        }
    }

    @Override
    public String name() {
        return "Separate";
    }

    @Override
    public int tier() {
        return 1;
    }
}
