package com.ethylol.magical_meringue.item.recipes;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.tileentity.SpinningWheelTileEntity;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Map;
import java.util.Optional;

public class SpinningWheelRecipe implements IRecipe<SpinningWheelTileEntity> {

    public static final IRecipeSerializer<SpinningWheelRecipe> SERIALIZER = new Serializer();
    public static final IRecipeType<SpinningWheelRecipe> TYPE = IRecipeType.register("magical_meringue:spinning_wheel_recipe");


    private Item input, output;
    private Color strandColor;
    private ResourceLocation id;

    public SpinningWheelRecipe(Item input, Item output, Color strandColor, ResourceLocation id) {
        this.input = input;
        this.output = output;
        this.strandColor = strandColor;
        this.id = id;
    }

    @Override
    public boolean matches(SpinningWheelTileEntity inv, World worldIn) {
        return inv.getItemstackCopy().getItem() == input;
    }

    @Override
    public ItemStack getCraftingResult(SpinningWheelTileEntity inv) {
        return new ItemStack(output);
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(output);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeType<?> getType() { return TYPE; }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public Color getStrandColor() {
        return strandColor;
    }


    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SpinningWheelRecipe> {

        public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(MagicalMeringueCore.MODID, "spinning_wheel_recipe");

        public Serializer() {
            this.setRegistryName(RESOURCE_LOCATION);
        }

        @Override
        public SpinningWheelRecipe read(ResourceLocation recipeId, JsonObject json) {
            Item input = getItem(json, "input");
            Item output = getItem(json, "output");
            Color strandColor = new Color(json.get("strandColor").getAsInt());
            return new SpinningWheelRecipe(input, output, strandColor, recipeId);
        }


        @Nullable
        @Override
        public SpinningWheelRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Item input = buffer.readItemStack().getItem();
            Item output = buffer.readItemStack().getItem();
            Color strandColor = new Color(buffer.readInt());
            return new SpinningWheelRecipe(input,output, strandColor , recipeId);
        }

        private Item getItem(JsonObject json, String s) {
            return ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get(s).getAsString())).getItem();
        }

        @Override
        public void write(PacketBuffer buffer, SpinningWheelRecipe recipe) {
            buffer.writeItemStack(new ItemStack(recipe.input));
            buffer.writeItemStack(new ItemStack(recipe.output));
            buffer.writeInt(recipe.strandColor.getRGB());
        }
    }
}
