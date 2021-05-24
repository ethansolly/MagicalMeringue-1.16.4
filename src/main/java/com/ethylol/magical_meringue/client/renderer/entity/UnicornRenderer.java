package com.ethylol.magical_meringue.client.renderer.entity;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.client.renderer.entity.model.UnicornModel;
import com.ethylol.magical_meringue.entity.UnicornEntity;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HorseMarkingsLayer;
import net.minecraft.client.renderer.entity.layers.LeatherHorseArmorLayer;
import net.minecraft.client.renderer.entity.model.HorseModel;
import net.minecraft.entity.passive.horse.CoatColors;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public final class UnicornRenderer extends MobRenderer<UnicornEntity, UnicornModel> {

    public UnicornRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new UnicornModel(), 1.1F);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(UnicornEntity entity) {
        return new ResourceLocation(MagicalMeringueCore.MODID, "textures/entity/unicorn.png");
    }
}
