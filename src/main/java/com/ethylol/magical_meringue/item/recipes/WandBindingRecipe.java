package com.ethylol.magical_meringue.item.recipes;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.item.ModItems;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WandBindingRecipe extends SpecialRecipe {

    public static final Serializer SERIALIZER = new Serializer();
    private final List<Ingredient> recipeItems;

    public WandBindingRecipe(ResourceLocation idIn) {
        super(idIn);

        this.recipeItems = new ArrayList<>();
        recipeItems.add(Ingredient.fromItems(ModItems.wand));
        recipeItems.add(Ingredient.fromItems(ModItems.spellbook));

    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        ItemStack bookStack = ItemStack.EMPTY;
        int i = 0;

        for(int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
                if (itemstack.getItem() == ModItems.spellbook) {
                    bookStack = itemstack;
                }
            }
        }

        return i == this.recipeItems.size() && (net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.recipeItems) != null)
                && !bookStack.isEmpty() && bookStack.hasTag() && bookStack.getTag().contains("spell");
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack bookStack = ItemStack.EMPTY;
        ItemStack ret = ItemStack.EMPTY;

        for(int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() == ModItems.spellbook) {
                    bookStack = itemstack;
                }
                if (itemstack.getItem() == ModItems.wand) {
                    ret = itemstack.copy();
                }
            }
        }

        //double check
        if (!bookStack.isEmpty()) {
            String spell = bookStack.getTag().getString("spell");
            CompoundNBT nbt;
            if (ret.hasTag()) {
                nbt = ret.getTag();
            }
            else {
                nbt = new CompoundNBT();
            }
            nbt.putString("spell", spell);
            ret.setTag(nbt);
        }

        return ret;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width*height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<WandBindingRecipe> {

        Serializer() {
            this.setRegistryName(new ResourceLocation(MagicalMeringueCore.MODID, "wand_binding"));
        }

        @Override
        public WandBindingRecipe read(ResourceLocation recipeId, JsonObject json) {
            return new WandBindingRecipe(recipeId);
        }

        @Nullable
        @Override
        public WandBindingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            return new WandBindingRecipe(recipeId);
        }

        @Override
        public void write(PacketBuffer buffer, WandBindingRecipe recipe) {
            
        }
    }
}
