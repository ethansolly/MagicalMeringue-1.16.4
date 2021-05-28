package com.ethylol.magical_meringue.event;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.block.ModBlocks;
import com.ethylol.magical_meringue.client.gui.CreativeTab;
import com.ethylol.magical_meringue.client.renderer.entity.UnicornRenderer;
import com.ethylol.magical_meringue.entity.CaveLordEntity;
import com.ethylol.magical_meringue.entity.ModEntities;
import com.ethylol.magical_meringue.entity.UnicornEntity;
import com.ethylol.magical_meringue.gen.feature.PlatoniumFeature;
import com.ethylol.magical_meringue.item.Spellbook;
import com.ethylol.magical_meringue.item.Wand;
import com.ethylol.magical_meringue.item.recipes.ModRecipes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistrationHandler {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        //Register blocks
        event.getRegistry().register(new Block(AbstractBlock.Properties.create(Material.ROCK)
                .setRequiresTool()
                .hardnessAndResistance(3.0f, 3.0f))
                .setRegistryName(MagicalMeringueCore.MODID, "tachium_ore"));
        event.getRegistry().register(new Block(AbstractBlock.Properties.create(Material.IRON)
                .setRequiresTool()
                .hardnessAndResistance(3.0f, 6.0f))
                .setRegistryName(MagicalMeringueCore.MODID, "tachium_block"));
        event.getRegistry().register(new Block(AbstractBlock.Properties.create(Material.IRON)
                .setRequiresTool()
                .hardnessAndResistance(50.0f, 1200.0f))
                .setRegistryName(MagicalMeringueCore.MODID, "platonium_block"));

        //Register tileentities
    }


    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        //Register items
        event.getRegistry().register(new Spellbook());
        event.getRegistry().register((new Wand()));
        event.getRegistry().register((new Item((new Item.Properties()).group(CreativeTab.TAB)).setRegistryName(MagicalMeringueCore.MODID, "tachium_ingot")));
        event.getRegistry().register((new Item((new Item.Properties()).group(CreativeTab.TAB)).setRegistryName(MagicalMeringueCore.MODID, "platonium_ingot")));


        //Register itemblocks
        registerItemBlock(ModBlocks.tachium_ore, (new Item.Properties()).group(CreativeTab.TAB), event);
        registerItemBlock(ModBlocks.tachium_block, (new Item.Properties()).group(CreativeTab.TAB), event);
        registerItemBlock(ModBlocks.platonium_block, (new Item.Properties()).group(CreativeTab.TAB), event);
    }

    @SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event) {

        //Register itemblock models

    }

    /*
    private static void registerBlock(Block block) {
        ForgeRegistries.BLOCKS.register(block);
    }*/

    private static void registerItemBlock(Block block, Item.Properties properties, RegistryEvent.Register<Item> event) {
        Item itemBlock = new BlockItem(block, properties).setRegistryName(block.getRegistryName());
        event.getRegistry().register(itemBlock);
    }

    /*
    private static void registerItem(Item item) {
        ForgeRegistries.ITEMS.register(item);
    }
     */

    @SubscribeEvent
    public static void registerRecipe(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        ResourceLocation loc = new ResourceLocation(MagicalMeringueCore.MODID, "wand_binding");

        //Registry.register(Registry.RECIPE_TYPE, loc, ModRecipes.WAND_BINDING_RECIPE_TYPE);
        event.getRegistry().register(ModRecipes.WAND_BINDING_RECIPE_SERIALIZER);
    }


    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityType<?>> event) {
        ModEntities.unicorn = EntityType.Builder.create(UnicornEntity::new, EntityClassification.CREATURE).size(1.3964844F, 1.6F).trackingRange(10).build("magical_meringue:unicorn");
        ModEntities.unicorn.setRegistryName(new ResourceLocation(MagicalMeringueCore.MODID, "unicorn"));
        event.getRegistry().register(ModEntities.unicorn);

        ModEntities.cave_lord = EntityType.Builder.create(CaveLordEntity::new, EntityClassification.MONSTER).size(0.6F, 1.99F).trackingRange(8).build("magical_meringue:cave_lord");
        ModEntities.cave_lord.setRegistryName(new ResourceLocation(MagicalMeringueCore.MODID, "cave_lord"));
        event.getRegistry().register(ModEntities.cave_lord);


    }

    @OnlyIn(Dist.CLIENT)
    public static void registerEntityRenderers() {
        EntityRendererManager manager = Minecraft.getInstance().getRenderManager();

        manager.register(ModEntities.unicorn, new UnicornRenderer(manager));
        manager.register(ModEntities.cave_lord, new SkeletonRenderer(manager));
    }

    //Ore/feature registration

    public static Feature platonium_feature;
    public static ConfiguredFeature<?, ?> ore_tachium, platonium_feature_configured;

    @SubscribeEvent
    public static void registerFeature(RegistryEvent.Register<Feature<?>> event) {

        platonium_feature = new PlatoniumFeature(NoFeatureConfig.field_236558_a_).setRegistryName(new ResourceLocation(MagicalMeringueCore.MODID, "platonium_feature"));
        event.getRegistry().register(platonium_feature);

        ore_tachium = registerConfiguredFeature("ore_tachium", Feature.ORE.withConfiguration(new OreFeatureConfig(new BlockMatchRuleTest(Blocks.SMOOTH_QUARTZ), ModBlocks.tachium_ore.getDefaultState(), 64)).range(256).square().func_242731_b(4));
        platonium_feature_configured = registerConfiguredFeature("platonium_feature", ((PlatoniumFeature)platonium_feature).withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).chance(1000).range(256));
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> registerConfiguredFeature(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, MagicalMeringueCore.MODID + ":" + name, configuredFeature);
    }

}
