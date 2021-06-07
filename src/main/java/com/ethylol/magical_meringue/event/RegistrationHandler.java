package com.ethylol.magical_meringue.event;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.block.ModBlocks;
import com.ethylol.magical_meringue.block.SpinningWheel;
import com.ethylol.magical_meringue.client.gui.CreativeTab;
import com.ethylol.magical_meringue.client.renderer.entity.UnicornRenderer;
import com.ethylol.magical_meringue.client.renderer.tileentity.SpinningWheelTileEntityRenderer;
import com.ethylol.magical_meringue.entity.CaveLordEntity;
import com.ethylol.magical_meringue.entity.ModEntityTypes;
import com.ethylol.magical_meringue.entity.UnicornEntity;
import com.ethylol.magical_meringue.gen.feature.PlatoniumFeature;
import com.ethylol.magical_meringue.item.Spellbook;
import com.ethylol.magical_meringue.item.Wand;
import com.ethylol.magical_meringue.item.recipes.ModRecipes;
import com.ethylol.magical_meringue.tileentity.ModTileEntityTypes;
import com.ethylol.magical_meringue.tileentity.SpinningWheelTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistrationHandler {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        //Register blocks

        event.getRegistry().register(ModBlocks.tachium_ore = new Block(AbstractBlock.Properties.create(Material.ROCK)
                .setRequiresTool()
                .hardnessAndResistance(3.0f, 3.0f))
                .setRegistryName(MagicalMeringueCore.MODID, "tachium_ore"));

        event.getRegistry().register(ModBlocks.tachium_block = new Block(AbstractBlock.Properties.create(Material.IRON)
                .setRequiresTool()
                .hardnessAndResistance(3.0f, 6.0f))
                .setRegistryName(MagicalMeringueCore.MODID, "tachium_block"));

        event.getRegistry().register(ModBlocks.platonium_block = new Block(AbstractBlock.Properties.create(Material.IRON)
                .setRequiresTool()
                .hardnessAndResistance(50.0f, 1200.0f))
                .setRegistryName(MagicalMeringueCore.MODID, "platonium_block"));

        event.getRegistry().register(ModBlocks.magentium_block = new Block(AbstractBlock.Properties.create(Material.WOOL)
                .hardnessAndResistance(0.8F)
                .notSolid()
                .setAllowsSpawn((a,b,c,d) -> false)
                .setOpaque((a,b,c) -> false)
                .setSuffocates((a,b,c) -> false)
                .setBlocksVision((a,b,c) -> false)
                .sound(SoundType.CLOTH))
                .setRegistryName(MagicalMeringueCore.MODID, "magentium_block"));

        event.getRegistry().register(ModBlocks.spinning_wheel = new SpinningWheel(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.WOOD)
                .hardnessAndResistance(2.0F)
                .notSolid()
                .setOpaque((a,b,c) -> false)
                .setSuffocates((a,b,c) -> false)
                .setAllowsSpawn((a,b,c,d) -> false)
                .setBlocksVision((a,b,c) -> false)
                .sound(SoundType.WOOD))
                .setRegistryName(MagicalMeringueCore.MODID, "spinning_wheel"));


        RenderTypeLookup.setRenderLayer(ModBlocks.magentium_block, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.spinning_wheel, RenderType.getCutout());

        //Register tileentities


    }


    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        //Register items
        event.getRegistry().register(new Spellbook());
        event.getRegistry().register((new Wand()));
        event.getRegistry().register((new Item((new Item.Properties()).group(CreativeTab.TAB)).setRegistryName(MagicalMeringueCore.MODID, "tachium_ingot")));
        event.getRegistry().register((new Item((new Item.Properties()).group(CreativeTab.TAB)).setRegistryName(MagicalMeringueCore.MODID, "platonium_ingot")));
        event.getRegistry().register(new Item((new Item.Properties()).group(CreativeTab.TAB)).setRegistryName(MagicalMeringueCore.MODID, "magentium_strand"));

        //Register itemblocks
        registerItemBlock(ModBlocks.tachium_ore, (new Item.Properties()).group(CreativeTab.TAB), event);
        registerItemBlock(ModBlocks.tachium_block, (new Item.Properties()).group(CreativeTab.TAB), event);
        registerItemBlock(ModBlocks.platonium_block, (new Item.Properties()).group(CreativeTab.TAB), event);
        registerItemBlock(ModBlocks.magentium_block, (new Item.Properties()).group(CreativeTab.TAB), event);
        registerItemBlock(ModBlocks.spinning_wheel, (new Item.Properties()).group(CreativeTab.TAB), event);



    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {

        //Register itemblock models

        //Register custom block model from OBJ
        ResourceLocation location = new ResourceLocation(MagicalMeringueCore.MODID, "block/spinning_wheel_wheel");
        ModelLoader.addSpecialModel(location);

        location = new ResourceLocation(MagicalMeringueCore.MODID, "block/spinning_wheel_base");
        ModelLoader.addSpecialModel(location);

        location = new ResourceLocation(MagicalMeringueCore.MODID, "block/spinning_wheel_strip");
        ModelLoader.addSpecialModel(location);

    }

    private static void registerItemBlock(Block block, Item.Properties properties, RegistryEvent.Register<Item> event) {
        Item itemBlock = new BlockItem(block, properties).setRegistryName(block.getRegistryName());
        event.getRegistry().register(itemBlock);
    }


    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().register(ModRecipes.WAND_BINDING_RECIPE_SERIALIZER);
        event.getRegistry().register(ModRecipes.SPINNING_WHEEL_RECIPE_SERIALIZER);
    }



    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        ModEntityTypes.unicorn = EntityType.Builder.create(UnicornEntity::new, EntityClassification.CREATURE).size(1.3964844F, 1.6F).trackingRange(10).build("magical_meringue:unicorn");
        ModEntityTypes.unicorn.setRegistryName(new ResourceLocation(MagicalMeringueCore.MODID, "unicorn"));
        event.getRegistry().register(ModEntityTypes.unicorn);

        ModEntityTypes.cave_lord = EntityType.Builder.create(CaveLordEntity::new, EntityClassification.MONSTER).size(0.6F, 1.99F).trackingRange(8).build("magical_meringue:cave_lord");
        ModEntityTypes.cave_lord.setRegistryName(new ResourceLocation(MagicalMeringueCore.MODID, "cave_lord"));
        event.getRegistry().register(ModEntityTypes.cave_lord);


    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        registerTileEntity(event, "spinning_wheel", ModTileEntityTypes.spinning_wheel, SpinningWheelTileEntity::new, ModBlocks.spinning_wheel);

    }

    public static void registerTileEntityRenderers() {
        ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.spinning_wheel, SpinningWheelTileEntityRenderer::new);
    }

    private static <T extends TileEntity> void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event, String key, TileEntityType<T> tileEntityType, Supplier<T> supplier, Block... blocks) {
        tileEntityType = TileEntityType.Builder.create(supplier, blocks).build(null);
        tileEntityType.setRegistryName(new ResourceLocation(MagicalMeringueCore.MODID, key));
        event.getRegistry().register(tileEntityType);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerEntityRenderers() {
        EntityRendererManager manager = Minecraft.getInstance().getRenderManager();

        manager.register(ModEntityTypes.unicorn, new UnicornRenderer(manager));
        manager.register(ModEntityTypes.cave_lord, new SkeletonRenderer(manager));

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
