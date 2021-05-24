package com.ethylol.magical_meringue.item.recipes;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;

public class ModRecipes {

    public static IRecipeSerializer<WandBindingRecipe> WAND_BINDING_RECIPE_SERIALIZER = new WandBindingRecipe.Serializer();
    public static WandBindingRecipeType WAND_BINDING_RECIPE_TYPE = new WandBindingRecipeType();



}
