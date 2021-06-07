package com.ethylol.magical_meringue.event;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.*;
import com.ethylol.magical_meringue.capabilities.join.IJoinHandler;
import com.ethylol.magical_meringue.capabilities.join.JoinHandler;
import com.ethylol.magical_meringue.capabilities.join.JoinMessage;
import com.ethylol.magical_meringue.capabilities.join.JoinProvider;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.capabilities.mana.ManaProvider;
import com.ethylol.magical_meringue.entity.CaveLordEntity;
import com.ethylol.magical_meringue.entity.ModEntityTypes;
import com.ethylol.magical_meringue.item.ModItems;
import com.ethylol.magical_meringue.utils.Utils;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.ArrayList;

import static com.ethylol.magical_meringue.event.RegistrationHandler.*;

@Mod.EventBusSubscriber()
public class EventHandler {

    //Capabilities
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(MagicalMeringueCore.MODID, "mana"), new ManaProvider());
            event.addCapability(new ResourceLocation(MagicalMeringueCore.MODID, "join"), new JoinProvider());
        }
    }


    //Other Events

    @SubscribeEvent
    public static void playerJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity && !event.getWorld().isRemote) {
            PlayerEntity player = (PlayerEntity) event.getEntity();

            //Add spellbook
            LazyOptional<IJoinHandler> joinHandlerLO = player.getCapability(Capabilities.JOIN_HANDLER_CAPABILITY, null);
            joinHandlerLO.ifPresent(joinHandler -> {
                if (!joinHandler.hasJoined()) {
                    ItemStack stack = new ItemStack(ModItems.spellbook);
                    ItemEntity entityItem = new ItemEntity(event.getWorld(), player.getPosX(), player.getPosY(), player.getPosZ(), stack);
                    entityItem.setNoPickupDelay();
                    event.getWorld().addEntity(entityItem);

                    joinHandler.setJoined(true);
                    MagicalMeringueCore.network.sendTo(new JoinMessage(joinHandler), ((ServerPlayerEntity) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
                }
            });

            LazyOptional<IManaHandler> manaHandlerLO = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            manaHandlerLO.ifPresent(manaHandler -> MagicalMeringueCore.network.sendTo(new ManaMessage(manaHandler), ((ServerPlayerEntity) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT));

        }
    }


    @SubscribeEvent
    public static void cloneEvent(PlayerEvent.Clone event) {
        /*
        NBTBase mana = event.getOriginal().getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null).serializeNBT();
        event.getEntityPlayer().getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null).deserializeNBT(mana);
         */
        if (event.isWasDeath()) {
            LazyOptional<IJoinHandler> joinLO = event.getOriginal().getCapability(Capabilities.JOIN_HANDLER_CAPABILITY, null);
            joinLO.ifPresent(join -> event.getPlayer().getCapability(Capabilities.JOIN_HANDLER_CAPABILITY, null).orElse(new JoinHandler()).setJoined(join.hasJoined()));
        }

    }

    @SubscribeEvent
    public static void playerWakeUp(PlayerWakeUpEvent event) {
        PlayerEntity player = event.getPlayer();
        World w = player.world;
        LazyOptional<IManaHandler> manaHandlerLO = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
        manaHandlerLO.ifPresent(manaHandler -> {
            int level = manaHandler.getLvl();
            for(int i = 0; i < manaHandler.getLvl(); i++) {
                manaHandler.setMana(i, Utils.maxMana(i, level));
            }

            if (!w.isRemote && w instanceof ServerWorld) {

                ResourceLocation resourcelocation = new ResourceLocation(MagicalMeringueCore.MODID, "astral_plane");
                RegistryKey<World> registrykey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, resourcelocation);
                ServerWorld astroworld = w.getServer().getWorld(registrykey);

                if (manaHandler.getCasterState() == IManaHandler.CasterState.ASTRAL) {
                    if (player.world != astroworld && astroworld != null) {
                        manaHandler.setCasterState(IManaHandler.CasterState.PORTAL);
                        Capabilities.sendManaMessageToClient(player, manaHandler);
                    }
                }
            }

        });
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            //End of tick
            if (!event.world.isRemote) {

                MinecraftServer server = event.world.getServer();
                ResourceLocation resourcelocation = new ResourceLocation(MagicalMeringueCore.MODID, "astral_plane");
                RegistryKey<World> registrykey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, resourcelocation);
                ServerWorld astroworld = server.getWorld(registrykey);

                for (PlayerEntity player : server.getPlayerList().getPlayers()) {
                    LazyOptional<IManaHandler> manaHandlerLO = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
                    manaHandlerLO.ifPresent(manaHandler -> {
                        if (manaHandler.getCasterState() == IManaHandler.CasterState.PORTAL) {

                            double x = player.getPosX();
                            double z = player.getPosZ();
                            astroworld.getChunk((int) x >> 4, (int) z >> 4);
                            double y = astroworld.getHeight(Heightmap.Type.WORLD_SURFACE, (int) x, (int) z);
                            ((ServerPlayerEntity) player).teleport(astroworld, x, y, z, player.rotationYaw, player.rotationPitch);
                            manaHandler.setCasterState(IManaHandler.CasterState.DEFAULT);
                            Capabilities.sendManaMessageToClient(player, manaHandler);

                        }
                    });
                }

                if ((event.world.getDayTime() == 24000 || event.world.getDayTime() == 0) && astroworld != null) {
                    for (ServerPlayerEntity spe : new ArrayList<>(astroworld.getPlayers())) {

                        ResourceLocation resourcelocationOverworld = new ResourceLocation("minecraft:overworld");
                        RegistryKey<World> registrykeyOverworld = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, resourcelocationOverworld);
                        ServerWorld overworld = server.getWorld(registrykeyOverworld);

                        BlockPos spawnPos = spe.func_241140_K_();
                        int x = spawnPos.getX();
                        int y = spawnPos.getY();
                        int z = spawnPos.getZ();
                        overworld.getChunk((int) x >> 4, (int) z >> 4);
                        float spawnAngle = spe.func_242109_L();
                        spe.teleport(overworld, x, y, z, 0, spawnAngle);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void subscribeBiomeLoading(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if (event.getName() != null && event.getName().toString().equals("magical_meringue:astral_plane")) {
            generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore_tachium);
            generation.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, platonium_feature_configured);
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        World world = (World) event.getWorld();
        if (!world.isRemote) {
            BlockState blockState = event.getPlacedBlock();
            Block block = blockState.getBlock();
            BlockPos pos = event.getPos();
            if (block == Blocks.CREEPER_HEAD || block == Blocks.CREEPER_WALL_HEAD) {
                if (world.getBlockState(pos.down(1)).getBlock() == Blocks.BONE_BLOCK && world.getBlockState(pos.down(2)).getBlock() == Blocks.BONE_BLOCK) {
                    world.removeBlock(pos, false);
                    world.removeBlock(pos.down(1), false);
                    world.removeBlock(pos.down(2), false);

                    CaveLordEntity caveLord = ModEntityTypes.cave_lord.create(world);
                    caveLord.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
                    caveLord.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.CREEPER_HEAD));
                    caveLord.setLocationAndAngles(pos.getX()+0.5, pos.getY()+0.55-2, pos.getZ()+0.5, 0.0F, 0.0F);
                    world.addEntity(caveLord);
                }
            } else if (block == Blocks.BONE_BLOCK) {
                if ((world.getBlockState(pos.up(1)).getBlock() == Blocks.CREEPER_HEAD || world.getBlockState(pos.up(1)).getBlock() == Blocks.CREEPER_WALL_HEAD) && world.getBlockState(pos.down(1)).getBlock() == Blocks.BONE_BLOCK) {
                    world.removeBlock(pos.up(1), false);
                    world.removeBlock(pos, false);
                    world.removeBlock(pos.down(1), false);

                    CaveLordEntity caveLord = ModEntityTypes.cave_lord.create(world);
                    caveLord.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
                    caveLord.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.CREEPER_HEAD));
                    caveLord.setLocationAndAngles(pos.getX()+0.5, pos.getY()+0.55-1, pos.getZ()+0.5, 0.0F, 0.0F);
                    world.addEntity(caveLord);
                }
                else if ((world.getBlockState(pos.up(2)).getBlock() == Blocks.CREEPER_HEAD || world.getBlockState(pos.up(2)).getBlock() == Blocks.CREEPER_WALL_HEAD) && world.getBlockState(pos.up(1)).getBlock() == Blocks.BONE_BLOCK) {
                    world.removeBlock(pos.up(2), false);
                    world.removeBlock(pos.up(1), false);
                    world.removeBlock(pos, false);

                    CaveLordEntity caveLord = ModEntityTypes.cave_lord.create(world);
                    caveLord.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
                    caveLord.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.CREEPER_HEAD));
                    caveLord.setLocationAndAngles(pos.getX()+0.5, pos.getY()+0.55, pos.getZ()+0.5, 0.0F, 0.0F);
                    world.addEntity(caveLord);
                }

            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().getType() == ModEntityTypes.cave_lord) {
            Entity trueSource = event.getSource().getTrueSource();
            if (trueSource != null && trueSource.getType() == EntityType.PLAYER) {
                PlayerEntity player = (PlayerEntity) trueSource;
                LazyOptional<IManaHandler> manaHandlerLO = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
                manaHandlerLO.ifPresent(manaHandler -> {
                    if (manaHandler.getLvl() == 1) {
                        manaHandler.setLvl(2);
                        for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
                            manaHandler.setMana(i, Utils.maxMana(i, 2));
                        }
                    }

                    Capabilities.sendManaMessageToClient(player, manaHandler);
                });
            }
        }
    }
}
