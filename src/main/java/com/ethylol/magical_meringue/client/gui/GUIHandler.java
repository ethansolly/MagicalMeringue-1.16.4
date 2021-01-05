package com.ethylol.magical_meringue.client.gui;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaHandler;
import com.ethylol.magical_meringue.utils.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MagicalMeringueCore.MODID)
public class GUIHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDrawScreenPre(RenderGameOverlayEvent.Pre event) {

    }

    @SubscribeEvent
    public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getType() == ElementType.ALL) {
            PlayerEntity player = mc.player;
            if (!player.isSpectator()) {
                LazyOptional<IManaHandler> manaHandlerLO = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
                manaHandlerLO.ifPresent(manaHandler -> {
                    MainWindow window = event.getWindow();
                    //int width = window.getScaledWidth();
                    int height = window.getScaledHeight();
                    int color = Utils.colorFromHexString("FFAA00");
                    MatrixStack stack = event.getMatrixStack();

                    mc.fontRenderer.drawStringWithShadow(stack,"Available Mana:", 10, 10, color);
                    for (int i = 0; i < manaHandler.getLvl(); i++) {
                        mc.fontRenderer.drawStringWithShadow(stack, "Tier " + (i + 1) + ": " + manaHandler.getMana(i), 20, 10 + (height - 20) * (i + 1) / 10, color);
                    }
                });

            }
        }
    }

}
