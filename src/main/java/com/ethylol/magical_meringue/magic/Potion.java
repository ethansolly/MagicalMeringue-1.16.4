package com.ethylol.magical_meringue.magic;

import net.minecraft.item.Item;

import java.util.List;

public class Potion {

    private ISpellEffect effect;
    private List<Item> ingredients;

    public Potion(ISpellEffect effect, List<Item> ingredients) {
        this.effect = effect;
        this.ingredients = ingredients;
    }

    public ISpellEffect getEffect() {
        return effect;
    }

    public List<Item> getIngredients() {
        return ingredients;
    }
}
