package com.ethylol.magical_meringue.event;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.item.Spellbook;
import com.ethylol.magical_meringue.item.Wand;
import com.ethylol.magical_meringue.item.recipes.ModRecipes;
import com.ethylol.magical_meringue.item.recipes.WandBindingRecipe;
import com.ethylol.magical_meringue.item.recipes.WandBindingRecipeType;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistrationHandler {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        //Register blocks

        //Register tileentities
    }


    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        //Register items
        registerItem(new Spellbook());
        registerItem(new Wand());
        //Register itemblocks

    }

    @SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event) {

        //Register itemblock models
    }

    private static void registerBlock(Block block) {
        ForgeRegistries.BLOCKS.register(block);
    }

    private static void registerItemBlock(Block block, Item.Properties properties) {
        Item itemBlock = new BlockItem(block, properties).setRegistryName(block.getRegistryName());
        ForgeRegistries.ITEMS.register(itemBlock);
    }

    private static void registerItem(Item item) {
        ForgeRegistries.ITEMS.register(item);
    }

    @SubscribeEvent
    public static void registerRecipe(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        ResourceLocation loc = new ResourceLocation(MagicalMeringueCore.MODID, "wand_binding");

        //Registry.register(Registry.RECIPE_TYPE, loc, ModRecipes.WAND_BINDING_RECIPE_TYPE);
        event.getRegistry().register(ModRecipes.WAND_BINDING_RECIPE_SERIALIZER);
    }
}
